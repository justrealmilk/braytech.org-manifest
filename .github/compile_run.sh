javac -d cleanFiles/bin -cp cleanFiles/lib/gson/*.jar -sourcepath cleanFiles/src cleanFiles/src/Main.java
java -cp cleanFiles/bin:cleanFiles/lib/gson/* Main $1