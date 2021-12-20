# Application supporting antibiotic treatment in hospital

The application is designed to analyse the susceptibility to antibiotics within the strains isolated from the infections and establish the most optimal empiric therapy for hospitalised patients.

## Stack
Java Spring, MySQL, React

## Backend
### Instalation for Ubuntu
https://spring.io/guides/gs/accessing-data-mysql/?fbclid=IwAR2iBqbrT-0ExXPz0lzsMMoOIDIDouh8QesojLCHU2AZ1uCN-3AvXKjpnlM#:~:text=Create%20the-,Database,-Open%20a%20terminal
```bash
sudo apt update
sudo apt install mysql-server

#open a MySQL client as a user who can create new users
sudo mysql --password
mysql> create database db_antibiotic; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_antibiotic.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```
Next for development install IntelliJ, open project and run the main class.
Use JDK17 and install

## Frontend
### Installation
To install needed npm packages run

```bash
cd frontend
npm i
```

### Usage
To use the frontend module run

```bash
npm start
```

### In case of errors
https://stackoverflow.com/questions/55763428/react-native-error-enospc-system-limit-for-number-of-file-watchers-reached?fbclid=IwAR2cf9tgvx09vHVw1yRKf-yad0yXCTEGGHeO2duhvwXlBzcroA67I_oc0v8
```quote
Solution:

Modify the number of system monitoring files

Ubuntu

sudo gedit /etc/sysctl.conf

Add a line at the bottom

fs.inotify.max_user_watches=524288

Then save and exit!

sudo sysctl -p

to check it
```
## Documentation
