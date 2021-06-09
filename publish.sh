#!/bin/bash
set -e

echo "$PGP_SECRET" | base64 --decode | gpg --import
if [[ $GITHUB_REF == refs/tags/* ]]; then
  command="; publishSigned; sonatypePrepare; sonatypeBundleUpload; sonatypeReleaseAll"
else
  command="publishSigned"
fi
echo "Running: sbt \"$command\""
exec sbt "$command"
