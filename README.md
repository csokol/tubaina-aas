tubaina-aas
=====================================

To run the app in development:

* Create conf/application.conf based on conf/application.conf.example
* In the play console:

    ~run -Demail.password=<email-password> -Dgithub.client_id=<github-client-id> -Dgithub.client_secret=<github-secret>

Deploy
======

* Run `play dist`
* Send the distribution to the server: `scp dist/tubaina-aas-1.0-SNAPSHOT.zip <user>@<server>:~/deploy`
* Go to the server and run:
    * `cd deploy`
    * `sudo deploy.sh`
