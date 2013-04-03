#!/usr/bin/bash

set -e

play dist
scp -i ~/.ssh/integracao-apostilas.pem dist/tubaina-aas-1.0-SNAPSHOT.zip  ec2-user@107.21.125.83:~/deploy
#ssh -i ~/.ssh/integracao-apostilas.pem ec2-user@107.21.125.83 /home/ec2-user/deploy/deploy.sh
