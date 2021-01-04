#!/bin/bash
#
# Time Lapse Maker Launcher
#

function run_app {
    _dir=$(dirname "$0")
    "${_java}" -jar "${_dir}/time-lapse-maker-@packageVersion@.jar"
    return $?
}

if type -p java > /dev/null; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "Error: Java not found"
    exit 1
fi

if [[ "${_java}" ]]; then
    java_version=$("${_java}" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "${java_version}" > "11" ]]; then
        run_app
        exit $?
    else
        echo "Error: Java ${java_version} found"
        echo "Java 11 or greater is needed to run this application"
        exit 1
    fi
fi
