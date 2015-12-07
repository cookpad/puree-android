#!/bin/bash
# ./deploy_javadoc.sh

set -ex

GH_PAGES_DIR=gh-pages

./gradlew puree:clean puree:bundleJavadocRelease

# Delete any existing temporary website clone
rm -rf $GH_PAGES_DIR

# Checkout and track the gh-pages branch
git clone . --branch gh-pages $GH_PAGES_DIR
cd $GH_PAGES_DIR

rm -rf *
unzip ../puree/build/libs/puree-*-javadoc.jar

# Stage all files in git and create a commit
git add .
git commit -m "Publish javadoc at $(LANG=en date)."

# Push the new files up to the parent repository
git push origin gh-pages

cd ..

# Push thew new files up to GitHub pages
git push origin gh-pages
