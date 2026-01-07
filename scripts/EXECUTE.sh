#!/bin/bash

# First argument should be name of screen.
echo "name of screen : $1"

# Second argument should be command to execute. Rember to add \n
echo "command to execute : $2"

screen -S $1 -X stuff $2