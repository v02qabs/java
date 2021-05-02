javac -cp ./libs/ftp4j.jar: ./MainActivity.java -d ./bin/.

cd ./bin

java -cp ../libs/ftp4j.jar: ftpFilelist

cd ../

