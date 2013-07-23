@echo off
title Project Insanity

"C:\Program Files (x86)\Java\jre7\bin\java.exe" -Xmx556m -cp bin;deps/GTLVote.jar;deps/poi.jar;deps/mysql.jar;deps/mina.jar;deps/slf4j.jar;deps/slf4j-nop.jar;deps/jython.jar;log4j-1.2.15.jar; server.Server
pause