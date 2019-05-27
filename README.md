# jsonplaceholder_tests

This is based upon cucumber-java-skeleton as a convenient starting point - see: https://github.com/cucumber/cucumber-java-skeleton

There is a single feature file with 4 scenarios. These cover a simple case for retrieval, creation, update and deletion of Posts against the AUT: https://jsonplaceholder.typicode.com

# Caveats:
The application under test does not persist changes. Whilst the response received will be correct, any subsequent requests will revert to the initial state. This limits the tests possible as it prevents the creation of specific data for retrieval etc.

# Observations:
In addition to the above lack of persistence:

Pagination would be a good addition - it currently returns the entire 100 preset records of posts on each request when no specific ID is provided in the request

It returns a cookie - this may indicate that it may be holding a session in memory, something I would investigate further


# Instructions for use:

## Pre-requisites:

# Java
Ensure Java 1.8+ is available
Java -version

If needed it can be obtained from here: https://www.java.com/en/download/

# Git
Confirm git is installed
git --version

If needed it can be obtained from here: https://git-scm.com/download/win

# Maven
Get Maven from: https://maven.apache.org/download.cgi?Preferred=http%3A%2F%2Fmirrors.ukfast.co.uk%2Fsites%2Fftp.apache.org%2F

Unzip it and add the unzip location + /bin to your path. E.g: "C:\Program Files\apache-maven-3.6.1\bin"

# Clone the repo into a suitable folder:
git clone https://github.com/davidgarratt/jsonplaceholder_tests.git

# Use Maven to execute the tests:

Open a command window navigate to the root of the project and run:

    mvn test

The test results will be displayed in the console.


