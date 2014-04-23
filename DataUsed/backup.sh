#!/bin/sh

# Create new backup
# note: no space between the -p and the password
mysqldump --opt -B Tec -u root -p > backup_Tec_`date +%y-%m-%d`.sql
