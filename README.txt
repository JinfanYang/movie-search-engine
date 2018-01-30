Movie Search Engine

1. Before starting deploy webpage on server, please change the parameter in engine.conf

change “corpus_prefix“ to your absolute address in where the corpus is
change “index_prefix” to your absolute address in where the index is

2. Change the code of SearchEngine.java and Indexer.java

SearchEngine.java: 
//OPTIONS = new Options("/Users/jinfanyang/Desktop/Project/Project/conf/engine.conf"); 
change the address of file engine.conf in this line

Indexer.java:
//SearchEngine.OPTIONS = new Options("/Users/jinfanyang/Desktop/Project/Project/conf/engine.conf");
change the address of file engine.conf in this line

Since our project used the static html page, dynamic JSP page and servlet, it will be easier to run the project in the eclipse.
But we provided access to crawler and indexer, you can run those two classes directly.
