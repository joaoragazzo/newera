#!/bin/bash


echo "[!] Entrando no diretório do backend"
cd /home/0x6a70/new_era/backend/alpha

mv /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties.test 
mv /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties.production /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties

echo "[!] Realizando o packing do backend"
mvn clean package > /home/0x6a70/new_era/logs/backendBuild.log

echo "[!] Packing realizado com sucesso"
echo "[!] Enviando o backend.jar para o diretório padrão (/var/www/backend/backend.jar)"
mv /home/0x6a70/new_era/backend/alpha/target/alpha-0.0.1-SNAPSHOT.jar /var/www/backend/backend.jar

mv /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties.production 
mv /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties.test /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties
chown 0x6a70:users /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties
chmod 750 /home/0x6a70/new_era/backend/alpha/src/main/resources/application.properties

chown -R 0x6a70:users /home/0x6a70/new_era/backend/alpha/target
chmod -R u+w /home/0x6a70/new_era/backend/alpha/target

echo "[!] Reiniciando o serviço do backend"
systemctl restart backend

echo "[!] Mudando as permissões do backend.jar"
chown www-data:www-data "/var/www/backend/backend.jar"
chmod 750 "/var/www/backend/backend.jar"

echo "[!] Entrando no diretório do frontend"
cd /home/0x6a70/new_era/frontend/alpha

echo "[!] Buildando o frontend"
npm run build > /home/0x6a70/new_era/logs/frontendBuild.log 2>&1

echo "[!] Removendo os arquivos antigos"
rm -rf /var/www/public_html/*

echo "[!] Colocando novos arquivos no diretório do frontend"
cp -r /home/0x6a70/new_era/frontend/alpha/build/* /var/www/public_html/

echo "[!] Reiniciando o NGINX..."
systemctl restart nginx