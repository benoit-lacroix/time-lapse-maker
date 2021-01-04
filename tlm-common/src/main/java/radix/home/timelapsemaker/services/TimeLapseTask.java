package radix.home.timelapsemaker.services;

import io.humble.video.Codec;
import io.humble.video.Encoder;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.PixelFormat;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Meter;
import radix.home.timelapsemaker.config.ImageComparator;
import radix.home.timelapsemaker.config.ImagePredicate;
import radix.home.timelapsemaker.enums.EnumFrameRate;
import radix.home.timelapsemaker.enums.EnumImageType;
import radix.home.timelapsemaker.utils.DurationUtils;
import radix.home.timelapsemaker.utils.GuiUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.humble.video.Coder.Flag.FLAG_GLOBAL_HEADER;
import static io.humble.video.ContainerFormat.Flag.GLOBAL_HEADER;
import static io.humble.video.awt.MediaPictureConverterFactory.HUMBLE_BGR_24;

@Slf4j
@AllArgsConstructor
public class TimeLapseTask extends Task<Void> {

    private final ImagePredicate imagePredicate = new ImagePredicate();
    private final ImageComparator imageComparator = new ImageComparator();
    private final String photosPath;
    private final String fileName;
    private final Meter progressBar;
    private final EnumFrameRate frameRate;
    private final Label status;

    @Override
    public Void execute() throws TaskExecutionException {
        try {
            long start = System.nanoTime();
            GuiUtils.updateProgressBar(progressBar, 0);
            GuiUtils.updateLabel(status, "Assembling");

            log.info("Starting time lapse making");
            log.info("Photos directory: {}", photosPath);

            Rational timeBase = Rational.make(frameRate.getNumerator(), frameRate.getDenominator());
            Muxer muxer = Muxer.make(fileName, null, null);

            final MuxerFormat format = muxer.getFormat();
            final Codec codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());

            Encoder encoder = Encoder.make(codec);

            File[] photos = new File(photosPath).listFiles();
            if (photos == null) {
                throw new FileNotFoundException("Directory not found: " + photosPath);
            } else if (photos.length == 0) {
                throw new FileNotFoundException("No photos found in: " + photosPath);
            }

            // After this assignment the photos are filtered by authorized type and ordered by name
            List<File> photoList = Arrays.stream(photos)
                    .parallel()
                    .filter(imagePredicate)
                    .sorted(imageComparator.byFileName())
                    .collect(Collectors.toList());

            log.info("{} photo(s) found", photoList.size());

            BufferedImage image = ImageIO.read(photoList.get(0));
            encoder.setWidth(image.getWidth());
            encoder.setHeight(image.getHeight());
            log.info("Encoder size: {}x{}", encoder.getWidth(), encoder.getHeight());

            // We are going to use 420P as the format because that's what most video formats these days use
            final PixelFormat.Type pixelFormat = PixelFormat.Type.PIX_FMT_YUV420P;
            encoder.setPixelFormat(pixelFormat);
            encoder.setTimeBase(timeBase);

            if (format.getFlag(GLOBAL_HEADER)) {
                encoder.setFlag(FLAG_GLOBAL_HEADER, true);
            }

            encoder.open(null, null);
            muxer.addNewStream(encoder);
            muxer.open(null, null);

            final MediaPacket packet = MediaPacket.make();

            mountImages(photoList, packet, muxer, encoder, progressBar);
            DurationUtils.logDurationInSeconds(start);

            GuiUtils.updateProgressBar(progressBar, 1);
            GuiUtils.updateLabel(status, "Encoding");

            log.info("Making final video");
            encodeVideo(encoder, packet, muxer);
            log.info("Video done");
            DurationUtils.logDurationInSeconds(start);
        } catch (Exception e) {
            log.error("An exception occurred", e);
            throw new TaskExecutionException(e);
        }
        return null;
    }

    /**
     * Method for mounting images together
     *
     * @param photos      List of the photos
     * @param packet      Packet of encoded data
     * @param muxer       Container of mediaPacket
     * @param encoder     Encoder
     * @param progressBar Progress bar
     * @throws IOException Exception thrown while reading input image
     */
    private void mountImages(List<File> photos, MediaPacket packet, Muxer muxer,
                             Encoder encoder, Meter progressBar) throws IOException {

        final MediaPictureConverter converter = MediaPictureConverterFactory.createConverter(HUMBLE_BGR_24,
                encoder.getPixelFormat(), encoder.getWidth(), encoder.getHeight());

        final MediaPicture picture = MediaPicture.make(encoder.getWidth(), encoder.getHeight(),
                encoder.getPixelFormat());
        picture.setTimeBase(encoder.getTimeBase());

        final double nbPhotos = photos.size();
        for (int i = 0; i < photos.size(); i++) {
            GuiUtils.updateProgressBar(progressBar, i / nbPhotos);

            log.info("Mounting photo: {}", photos.get(i).getName());
            if (!EnumImageType.JPG.matches(photos.get(i).toPath())) {
                log.warn("Not an image {}", photos.get(i).getName());
            } else if (photos.get(i).length() == 0) {
                log.warn("Empty image {}", photos.get(i).getName());
            } else {
                final BufferedImage image = ImageIO.read(photos.get(i));
                converter.toPicture(picture, image, i);
                do {
                    encoder.encode(packet, picture);
                    if (packet.isComplete())
                        muxer.write(packet, false);
                } while (packet.isComplete());
            }
        }
    }

    /**
     * Method for encoding output video
     *
     * @param encoder Encoder
     * @param packet  Packet of encoded data
     * @param muxer   Container of mediaPacket
     */
    private void encodeVideo(Encoder encoder, MediaPacket packet, Muxer muxer) {
        do {
            encoder.encode(packet, null);
            if (packet.isComplete()) {
                muxer.write(packet, false);
            }
        } while (packet.isComplete());
        muxer.close();
    }
}
