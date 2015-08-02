# gramar

noun **|** gram - ar **|** \ˈgra-mər\

Simply put, a gramar is a tool that can generate up to 95% (historically speaking, ex business logic) of a component for almost any given software architecture.  Past examples of supported architectures include:

1. Hadoop applications (Storm topologies, Mapreduce job flows, Kafka components)
2. Database persistence layers, both relational and NoSQL (HBase, Cassandra, Elasticsearch)
3. J2EE programming models (Servlets, EJB's, portlets, JSP tag libraries)
4. Eclipse tools (Text editors, Multi-page editors, UML schemas, Popup actions)
5. Gramars themselves

To be a bit more formal, a gramar is a set of production rules for source and other files in a component implementation adhering to a specific software architecture. Each gramar has an associated schema that models the allowed points of variability between instances of that software architecture. Each production describes how to generate a file from those points of variability and is often applied multiple times, depending on the cardinality of the model. A grammar does not describe the meaning of the generated files nor what can be done with them.  It only captures and generates their content.

The Gramar toolset supports the creation, authoring, distribution and application of individual gramars accross a number of IDE's and runtime environments.

A gramar consists of a set of files:

1. **gramar.config** provides the minimal metadata required during the gramar lifecycle
2. **main.prod** is the primary production.  It applies any required naming convention rules and then invokes other .prod files as needed.  Generally speaking, the main.prod production is the only production that creates (or replaces) projects, folders and files, but it delegates the content generation for those files to other .prod files. 
3. Other .prod files for file content generation
 
Gramars can be deployed:

1. As a set of files, either on a file system or on the classpath of a java application.  Extensions can be written to source individual files from other locations, such as via HTTP or from a data base
2. As a single zip file, again deployed on the file system or the classpath
3. As a jar file

## Productions

A production is a text file whose boilerplate content generally resembles the file content it's meant to generate.  Imbedded in its text content are gramar tags that can:

1. Read data from the model and insert in-place within the template boilerplate
2. Transform and write data back into the model
3. Conditionally process nested boilerplate and tags.  The conditional processing includes **if**, **case** and **iterative** behaviors.
4. Create projects, folders and files

As such, the processing of a production is effectively top-down procedural with possible side effects to the model and, in the case of **main.prod**, the creation of projects, folders and files.

## Background

Gramar is a next-generation greenfield implementation of both Eclipse Model-to-Text JET and, before that, the Design pattern Toolkit from IBM.  A gramar can be applied not just in Eclipse, but in IntelliJ, as a stand-alone java application, within a web application or in any other environment.
