javac -d "." -cp "cleanFiles/lib/jackson/*" "cleanFiles/src/CleanFiles.java"
java -cp ".:cleanFiles/lib/jackson/*" CleanFiles $1