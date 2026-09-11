#!/bin/sh

set -e

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
BUILD_DIR="$ROOT_DIR/target/Shell-In-Java-build"

cd "$ROOT_DIR"
mvn -B clean package -Ddir="$BUILD_DIR"
exec java -jar "$BUILD_DIR/Shell-In-Java.jar" "$@"
