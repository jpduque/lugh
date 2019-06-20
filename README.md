# Lugh

Lugh is a tool that helps analyzing your project stored on [GitHub](https://github.com) by retrieving a forecasted estimation of how many bugs would your project have in the next commit.


## Build

Lugh is built using [Maven](https://maven.apache.org/). If you do not have maven please follow the instructions to [Install Maven](https://maven.apache.org/download.cgi).

In order to generate the executable jar, in your command line tool you must run `mvn clean install`, once it has finished, the executable jar can be found on the target folder.

## Usage

In order to run Lugh and perform the analysis the following command should be run on the command line tool `java -jar Lugh.jar {Repository Owner} {Repository Project Name}`.

## Results

The results of the test will be displayed on the console as follows

```sh
┌────────────┬──────────────┬───────────┬───────────┬───────┬────────┬────────────┬───────┬──────────────┬──────┐
│BAD_PRACTICE│MALICIOUS_CODE│PERFORMANCE│CORRECTNESS│STYLE  │SECURITY│EXPERIMENTAL│NOISE  │MT_CORRECTNESS│I18N  │
├────────────┼──────────────┼───────────┼───────────┼───────┼────────┼────────────┼───────┼──────────────┼──────┤
│11          │2             │13         │6          │7      │0       │0           │0      │0             │3     │
└────────────┴──────────────┴───────────┴───────────┴───────┴────────┴────────────┴───────┴──────────────┴──────┘
```

All the descriptions retrieved are based on the [Spotbugs Bug Classification](https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html)
