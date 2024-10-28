# PIIMask: A masking library for sensitive personal identification information (PII)

[![PII Mask CI with Maven](https://github.com/Vicynet/pii-mask/actions/workflows/maven.yml/badge.svg)](https://github.com/Vicynet/pii-mask/actions/workflows/maven.yml)

## Overview

PIIMask is a lightweight Java library that masks Personally Identifiable Information (PII) fields within Java applications. Built for integration with Spring Boot, PIIMask allows developers to secure sensitive data by defining custom masking rules through annotations. Ideal for protecting data such as phone numbers, emails, and credit card information in JSON responses, PIIMask provides extensive customization options for masking patterns.

## Features

- **Flexible masking options**: Mask start, end, middle, or both ends of sensitive fields.
- **Annotation-based configuration**: Simple `@MaskData` annotation to define masking directly on model fields.
- **Spring Boot Integration**: Masking applies seamlessly in JSON serialization.

## Getting Started

### 1. Add Dependency
To add PIIMask to your project, include it as a Maven dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>dev.vicynet</groupId>
    <artifactId>piimask</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Usage
To mask sensitive fields, annotate them with `@MaskData` in your data models, specifying options like masking length and dimension (start, end, middle, or both ends).

### Example Usage

#### Annotating Fields
```java
import dev.vicynet.piimask.annotation.MaskData;

public class User {
    @MaskData(type = MaskDataE.MASK_START, length = 4)
    private String phoneNumber;

    @MaskData(type = MaskDataE.MASK_END, length = 6)
    private String creditCardNumber;

    @MaskData(type = MaskDataE.MASK_MIDDLE, length = 3)
    private String email;

    // Getters and setters
}
```

#### Customizable Masking Parameters
The `@MaskData` annotation supports the following parameters:

- `length`: Specifies the number of characters to mask.
- `type`: Defines which part of the field to mask:
  - `start`: Masks characters from the beginning.
  - `end`: Masks characters from the end.
  - `startend`: Masks both the start and end, leaving a middle segment visible.
  - `middle`: Masks a section in the middle.

#### Sample JSON Response
Given the above `User` class and sample values, a JSON response from a Spring Boot application might look like:

```json
{
  "phoneNumber": "****5678",
  "creditCardNumber": "1234******",
  "email": "tester@e***ple.com"
}
```
