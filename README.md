# gramar

A gramar is a set of production rules for source and other files in a component implementation adhering to a specific software architecture. Each gramar has an associated schema that models the allowed points of variability between instances of that software architecture. Each production describes how to generate a file from those points of variability and is often applied multiple times, depending on the cardinality of the model. A grammar does not describe the meaning of the generated files nor what can be done with them.  It only captures and generates their content.

The Gramar toolset supports the creation, authoring, distribution and application of individual gramars accross a number of IDE's and runtime environments.

**Gramar** 

noun **|** gram - ar **|** \ˈgra-mər\

1. A specific grammatical architecture with an associated schema describing points of variability
 
2. A normalization of a reference implementation represented as a set of file productions defined on each element type of the schema for the reference implementaion's points of variability.  

3. A formal description of a software architecture captured as a set of deployable code generation templates. 

## Background

Gramar is a next-generation greenfield implementation of both Eclipse Model-to-Text JET and, before that, the Design pattern Toolkit from IBM.  A gramar can be applied not just in Eclipse, but in IntelliJ, as a stand-alone java application, within a web application or in any other environment.
