javac -d "." -cp "cleanFiles/lib/*" -sourcepath "cleanFiles/src/" "cleanFiles/src/CleanFiles.java"
java -cp ".:cleanFiles/lib/*" -Dfile.encoding=UTF-8 CleanFiles $1