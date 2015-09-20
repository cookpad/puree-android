#!/bin/bash

set -ex

DIR=temp-clone

# Delete any existing temporary website clone
rm -rf $DIR

# Clone the current repo into temp folder
git clone $REPO $DIR

# Move working directory into temp folder
cd $DIR

# Checkout and track the gh-pages branch
git checkout -t origin/gh-pages

# Delete everything
rm -rf *

# Download the latest javadoc
curl -L "https://bintray.com/artifact/download/cookpad/maven/com/cookpad/puree/puree/${1}/puree-${1}-javadoc.jar" > javadoc.zip
unzip javadoc.zip
rm javadoc.zip

# Stage all files in git and create a commit
git add .
git add -u
git commit -m "Publish javadoc at $(date)."

# Push the new files up to GitHub
git push origin gh-pages

# Delete our temp folder
cd ..
rm -rf $DIR