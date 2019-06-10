
# OpenAPItoUML

![OpenAPItoUML](https://opendata-for-all.github.io/openapi-to-uml/images/logo.png)

A tool to generate UML models from OpenAPI definitions.

## Requirements
To generate UML models:
- Eclipse Modeling tools (it can be found [here](http://www.eclipse.org/downloads/packages/eclipse-modeling-tools/oxygen2)).

To visualize the generated UML models:
- A UML 2.5 modeling environment in Eclipse such as [Papyrus](https://www.eclipse.org/papyrus/) or [UMLDesigner](https://marketplace.eclipse.org/content/uml-designer) (we tested the tool with Papyrus).

## Installation
1. Open Eclipse IDE
2. Click on *Help / Install New Software...*
3. Click on *Add...* and fill in the form as indicated (the update site is https://opendata-for-all.github.io/openapi-to-uml/update/openapi-to-uml) then click on *OK*.

![Add repository](https://opendata-for-all.github.io/openapi-to-uml/images/add-eclipse.PNG)

4. Select *OpenAPI to UML* then click on *Next*.

![Install](https://opendata-for-all.github.io/openapi-to-uml/images/install-eclipse.PNG)

5. Follow the the rest of the steps (license, etc...) and reboot Eclipse.

## Using the plugin

1. Create a Project or use an existing project in your workspace.
2. Import the JSON file of your OpenAPI definition. 
3. Right-click on the definition file and select *OpenAPI to UML/Generate a Class diagram*. This will generate the UML model corresponding to the input definition under the folder *src-gen* of your project (check [petstore.uml](https://raw.githubusercontent.com/SOM-Research/openapi-to-uml/master/examples/edu.uoc.som.openapitouml.example/src-gen/petstore.uml), the generated model from the Petstore example).

## Visualizing the Class diagram using Papyrus

The Petstore example:
![Petstore](https://opendata-for-all.github.io/openapi-to-uml/images/petstore.png)

Instructions:

1. Install Papyrus if you didn't do it yet (You can find the instructions [here](https://www.eclipse.org/papyrus/download.html)).
2. Open the perspective *Papyrus*.
3. Right-click on the generated UML model and select *New -> Papyrus Model*.
4. Follow the steps in the wizard to initialize a Class diagram (keep everything as predefined except in the *Initializtion information* step where you should check *Class Diagram* as the Respresentation kind).
5. Drag-and-drop the UML elements from the *Model Expoler* into the editor.
6. Align and arrange the layout as you prefer.
7. Save.

## Notes
- Each schema definition  (#/definitions) of type `object` is represented as a class.
- All associations are of type aggregation.
- The location of an operation (i.e., in which class it should be) is decided based on the schema this operation produces (response 2xx schema), the schema it consumes (parameter of type body), or the tags properties of the operation. When no class is a good fit for the operation, an artificial class is created to host the operation. The name of such class is inferred from the path of the operation.
- The name of an operation is taken from `operationId` of the operation definition. If such information is not provided the name is created by concatenating the method of the operation (e.g., get, post) plus the name of its class.
- The cardinalities of attributes and parameters are inferred from:
	- the `type` field (`array` for multivalued)
	- the `required` field in the OpenAPI definition (note that a required parameter or property of type `array` doesn't not mean that the lower bound should be 1. Empty arrays are still valid).
	- `minItems` and `maxItems` for `array` types.

N.B. This tool relies on the OpenAPI metamodel located [here](https://github.com/SOM-Research/openapi-metamodel).
