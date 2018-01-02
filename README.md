# Environment to property merger

A missing link between environment variables and *JAVA* .properties files.

## Build

`git clone https://github.com/trajakovic/environment-to-property-merger.git && cd ./environment-to-property-merger && ./gradlew assemble bootRepackage`

Resulting jar is in ./build/libs

## Usage

  * ```
       $ PREFIX_THIS_IS_PROPERTY_FROM_ENVIRONMENT="Prefixed with PREFIX_, but case is ignored" \
           java -jar ./build/libs/environment-to-property-merger-2.0.0.jar ./example/example1.properties PREFIX_
     ```
      * output: 
          ```
          # INFO: Using environment variable prefix: prefix_
          # MERGED_WITH: https://github.com/trajakovic/environment-to-property-merger
          #Tue Jan 02 17:39:28 CET 2018
          this.is.property.from.environment=Prefixed with PREFIX_, but case is ignored
          second.property.in.example1=this is second line
          first.property.from.example1.file=just for demonstration
          ```
 * ```
    $ PREFIX_SECOND_PROPERTY_IN_EXAMPLE1="Property from environment variable is overriding existing one" \
       java -jar ./build/libs/environment-to-property-merger-2.0.0.jar ./example/example1.properties PREFIX_
    ```
      * output:
         ```
         # INFO: Using environment variable prefix: prefix_
         # MERGED_WITH: https://github.com/trajakovic/environment-to-property-merger
         #Tue Jan 02 17:46:29 CET 2018
         second.property.in.example1=Property from environment variable is overriding existing one
         first.property.from.example1.file=just for demonstration
         ```
 * ```
   $ PREFIX_MISSING_FILE_IS_OK=ok PREFIX_SECOND_PROPERTY=second \
       java -jar ./build/libs/environment-to-property-merger-2.0.0.jar /missing/property/file PREFIX_
   ``` 
     * output:
        ```
        # INFO: Missing input file /missing/property/file
        # INFO: Using environment variable prefix: prefix_
        # MERGED_WITH: https://github.com/trajakovic/environment-to-property-merger
        #Tue Jan 02 17:50:07 CET 2018
        missing.file.is.ok=ok
        second.property=second
        ```
 * ```
   $ USE_MINUS_TO_DENOTE_EMPTY_PREFIX=- \
       java -jar ./build/libs/environment-to-property-merger-2.0.0.jar /missing/property/file -
   ``` 
     * output:
       ```
        ye, here you'll get all environment variables as property files :)
       ```
 

