javac -d cleanFiles/bin -cp cleanFiles/lib/gson/*.jar -sourcepath cleanFiles/src cleanFiles/src/CleanFiles.java
java -cp cleanFiles/bin:cleanFiles/lib/gson/* -Dfile.encoding=UTF-8 CleanFiles $1