wget https://github.com/ThilinaManamgoda/jetcd/archive/master.zip
unzip master.zip -d .
cd jetcd-master
mvn clean install -DskipTests=true -Dmaven.javadoc.skip=true -B -V
