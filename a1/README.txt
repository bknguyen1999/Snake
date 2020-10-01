Brandon Nguyen
20733209 bknguyen
openjdk version "11.0.8" 2020-10-02
macOS 10.15.6 (MacBook Pro 2017)


Requirements:
- filetofind must be the first command line argument giving to FindFiles


Technical Notes:
- When -ext and -reg are both present in the command line argument, both conditions must be met for a file to be found (i.e. the file name must match the regex expression given and must end with one of the extensions provided).

ex. 
java FindFiles "^.*\.txt$" -reg -ext "txt,pdf"

If the current directory contains files: anything.txt
					 anything.txt.txt
					 anything.txt.pdf
					 hello.txt
					 hello.pdf
					 hello.jpg


FindFiles will return: anything.txt, anything.txt.txt, hello.txt

(See Professor Jeff Avery's answer on Piazza post @102 and the endorsed answer from post @127 for more details) 

