#!/bin/bash

# First argument should be name of screen.
echo "name of screen : $1"

command='stop\n'

screen -S $1 -X stuff $command
