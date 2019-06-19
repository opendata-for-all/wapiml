
# WAPIml

An OpenAPI round-trip tool that leverages on model-driven techniques to create, visualize, manage, and generate OpenAPI
definitions. WAPIml embeds an OpenAPI metamodel but also a UML profile to enable working with Web APIs in any UML-compatible modeling tool

N.B. The legacy tool **OpenAPItoUML**, which generates UML models from OpenAPI definitions, can be found under the branch *openapi-to-uml*.

## Requirements

Coming soon...

## Installation

Coming soon...

## Using the plugin

Coming soon...

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

N.B. This tool relies on the OpenAPI metamodel located [here](https://github.com/opendata-for-all/openapi-metamodel).
