# OpenAPItoUML

![OpenAPItoUML](https://som-research.github.io/openapi-to-uml/images/logo.png)

A tool to generate UML models from Open API defintions.

## Requirements
To generate UML models:
- Eclipse Modeling tools (it can be found [here](http://www.eclipse.org/downloads/packages/eclipse-modeling-tools/oxygen2)).

To visualize the generated UML models:
- A UML 2.5 modeling environment in Eclipse such as [Papyrus](https://www.eclipse.org/papyrus/) or [UMLDesigner](https://marketplace.eclipse.org/content/uml-designer) (we tested the tool with Papyrus).

## Installation
1. Open Eclipse IDE
2. Click on *Help / Install New Software...*
3. Click on *Add...* and fill in the form as indicated (the update site is https://som-research.github.io/openapi-to-uml/update/) then click on *OK*.

![Add repository](https://som-research.github.io/openapi-to-uml/images/add-eclipse.PNG)

4. Select *OpenAPI to UML* then click on *Next*.

![Install](https://som-research.github.io/openapi-to-uml/images/install-eclipse.PNG)

5. Follow the the rest of the steps (license, etc...) and reboot Eclipse.

## Using the plugin

1. Create a Project or use an existing project in your workspace.
2. Import the JSON file of your OpenAPI definition. 
3. Right-click on the definition file and select *OpenAPI to UML/Generate a Class diagram*. This will generate the UML model corresponding to the input definition under the folder *src-gen* of your project (Check [petstore.uml](https://raw.githubusercontent.com/SOM-Research/openapi-to-uml/master/examples/edu.uoc.som.openapitouml.example/src-gen/petstore.uml), the generated model from the Petstore example).

## Visualizing the Class diagram using Papyrus

The Petstore example:
![Petstore](https://som-research.github.io/openapi-to-uml/images/petstore.png)

Instructions:

1. Install Papyrus if you didn't do it yet (You can find the instructions [here](https://www.eclipse.org/papyrus/download.html)).
2. Open the perspective *Papyrus*.
3. Right-click on the generated UML model and select *New -> Papyrus Model*.
4. Follow the steps in the wizard to initialize a Class diagram (keep everything as predefined except in the *Initializtion information* step where you should check *Class Diagram* as the Respresentation kind).
5. Drag-and-drop the UML elements from the *Model Expoler* into the editor.
6. Align and arrange the layout as you wish.
7. Save.


