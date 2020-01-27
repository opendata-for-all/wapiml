# Changelog

All notable changes to WAPIml will be documented in this file.

The changelog format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Support the creation of UML model in standalone mode (without Eclipse context).

## [1.1.0] - 2019-11-04

### Added

- Discover foreign keys and transform them to associations.
- Create a wizard to guide the generation of UML models.
- Support YAML format.

### Fixed

- Enrich the error message displayed when an OpenAPI definition in not valid by giving more details about the error.


## [1.0.0] - 2019-07-15

### Added

- Build the project using Tycho.
- Generate OpenAPI definitions from UML models annotated with the OpenAPI profile.
- Apply the OpenAPI profile.

### Changed

- Rename the project to WAPIml. The old project OpenAPItoUML, which only covers the generation of UML models from OpenAPI definitions, can be found under the branch [openapi-to-uml](https://github.com/opendata-for-all/wapiml/tree/openapi-to-uml).

### Fixed

- [#4]: _Primitive types defined in the definitions objects are ignored_.
- [#3]: _Enumeration generated from schema objects having allOf contain "null" prefix_.
- [#2]: _Associations not generated for schema objects having allOf_.


[unreleased]: https://github.com/opendata-for-all/wapiml/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/opendata-for-all/wapiml/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/github.com/opendata-for-all/wapiml/releases/tag/v1.0.0


[#2]: https://github.com/opendata-for-all/wapiml/issues/2
[#3]: https://github.com/opendata-for-all/wapiml/issues/3
[#4]: https://github.com/opendata-for-all/wapiml/issues/4

