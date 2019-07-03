
# WAPIml

An OpenAPI round-trip tool that leverages on model-driven techniques to create, visualize, manage, and generate OpenAPI
definitions. WAPIml embeds an OpenAPI metamodel but also a UML profile to enable working with OpenAPI in any Eclipse UML-compatible modeling tool.

N.B. The legacy tool **OpenAPItoUML**, which generates UML models from OpenAPI definitions, can be found under the branch *openapi-to-uml*.

## Requirements

- Eclipse Modeling tools (it can be found [here](https://www.eclipse.org/downloads/packages/release/2019-06/r/eclipse-modeling-tools)).
- [Papyrus](https://www.eclipse.org/papyrus/) to visualize the UML model and manage the profile.

## Installation

1. Open Eclipse IDE
2. Click on *Help / Install New Software...*
3. Click on *Add...* and fill in the form as indicated (the update site is https://opendata-for-all.github.io/wapiml/update/wapiml) then click on *OK*.

![Add repository](https://opendata-for-all.github.io/wapiml/images/wapiml/capture1.PNG)

4. Select *WAPIlml* then click on *Next*.

![Install](https://opendata-for-all.github.io/wapiml/images/wapiml/capture2.PNG)

5. Follow the the rest of the steps (license, etc...) and reboot Eclipse.

## Using the plugin

###### Generating UML models 
1. Create a Project or use an existing project in your workspace.
2. Import the JSON file of your OpenAPI definition. 
3. Right-click on the definition file and select:

	a. *WAPIml/Generate a Class diagram* to generate a simple UML model.
	
	b. *WAPIml/Generate a Class diagram and apply the OpenAPI profile* to generate a UML model annotated with OpenAPI profile.
	
The generated models are located under the folder *src-gen*

###### Modeling OpenAPI definitions using Papyrus

From a generated model:

1. Open the perspective *Papyrus*.
2. Right-click on the generated UML model and select *New -> Papyrus Model*.
4. Follow the steps in the wizard to initialize a Class diagram (keep everything as predefined except in the *Initializtion information* step where you should check *Class Diagram* as the Respresentation kind).
5. Drag-and-drop the UML elements from the *Model Expoler* into the editor.
6. Align and arrange the layout as you prefer.
7. Save.

![Petstore](https://opendata-for-all.github.io/wapiml/images/wapiml/petstore.png)

From scratch:

1. Open the perspective *Papyrus*.
2. Click on *File* then select *New -> Papyrus Model*.
3. Follow the steps in the wizard to initialize a Class diagram (keep everything as predefined except in the *Initializtion information* step where you should check *Class Diagram* as the Respresentation kind and click on *Browse Registrered Profiles* and select *OpenAPI* as shown below).

![profile](https://opendata-for-all.github.io/wapiml/images/wapiml/capture3.PNG)

4. Add UML elements to the canvas.
5. Use the the *Properties* view to apply the streotypes and et the tag values (see an example below).

![Propertis views](https://opendata-for-all.github.io/wapiml/images/wapiml/capture4.PNG)

###### Generate an OpenAPI definition from an annotated UML model

1. Switch to the perspective *Java*.
2. Right-click on the annotated UML model and select *WAPIml -> Generate an OpenAPI definition*.

The generated OpenAPI definition will be located under the folder *src-gen* in JSON format.

## Notes
- Each schema definition  (#/definitions) of type `object` is represented as a class.
- All associations are of type aggregation.
- The location of an operation (i.e., in which class it should be) is decided based on the schema this operation produces (response 2xx schema), the schema it consumes (parameter of type body), or the tags properties of the operation. When no class is a good fit for the operation, an artificial class is created to host the operation. The name of such class is inferred from the path of the operation.
- The name of an operation is taken from `operationId` of the operation definition. If such information is not provided the name is created by concatenating the method of the operation (e.g., get, post) plus the name of its class.
- The cardinalities of attributes and parameters are inferred from:
	- the `type` field (`array` for multivalued)
	- the `required` field in the OpenAPI definition (note that a required parameter or property of type `array` doesn't not mean that the lower bound should be 1. Empty arrays are still valid).
	- `minItems` and `maxItems` for `array` types.

N.B. This tool relies on the OpenAPI metamodel located [here](https://github.com/opendata-for-all/openapi-metamodel).
