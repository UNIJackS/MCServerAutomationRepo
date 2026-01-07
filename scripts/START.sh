#!/bin/bash

# First argument should be name of screen.
echo "name of screen : $1"

# Second argument should be path.
echo "path to mc server : $2"

# starts server on a detached screen.
screen -dmS $1 $2