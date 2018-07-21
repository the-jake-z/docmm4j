# Docmm4j
[![Build Status](https://travis-ci.org/jzarob/docmm4j.svg?branch=master)](https://travis-ci.org/jzarob/docmm4j)
[![Coverage Status](https://coveralls.io/repos/github/jzarob/docmm4j/badge.svg?branch=master)](https://coveralls.io/github/jzarob/docmm4j?branch=master)

---

_Docmm4j_ is a tool for organizations of all sizes to automate the tedious task of creating
personalized PDFs from document templates and custom data sources.

---

> Mail merge is a process to create personalized letters and 
> pre-addressed envelopes or mailing labels for mass mailings from a form letter.
>> Cambridge Online Dictionary

---

_Docmm4j_ is a super easy way for your organization to start creating personalized PDFs from
document templates that utilize merge fields.

---

You can start by creating a document in the document library by `POST`ing to the 
`/api/v1/document` endpoint with a request body like this:

```json
    "documentNumber": "12345",
    "mapping": {
        "MergeFieldName": "$.json.path",
        "MergeField2Name": "$.another.path"
    }
```

The mapping links the merge field to a JSON value provided in the `mergeData` property of
either an individual or multiple merge request. We use the [JSONPath](https://github.com/json-path/JsonPath) library
to parse the `mergeData` object for the corresponding mappings.

You can upload the template for the document you just created by `POST`ing to the
`/api/v1/document/{documentNumber}/template`, where `{documentNumber}` is the value
provided in the JSON document in your initial post request. The template should
be uploaded as a multipart form file.

To perform a single, real-time document merge, perform a `POST` operation to 
`/api/v1/merge/single` with a request body like this:

```json
{
  "documentNumber": "12345",
  "mergeData": {
    "json": {
      "path": "some value"
    },
    "another": {
      "path": "some other value"
    }
  }
}
```

The single merge will return a single PDF in the content of the response. If you'd like
to merge several documents at the same time, you can do so by `POST`ing to 
`/api/v1/merge/multiple` with a request body like this:

```json
{
  "groupings": [
    {
      "name": "SampleName",
      "mergeRequests": [
        {
          "documentNumber": "12345",
          "mergeData": {
          "json": {
            "path": "some value"
          },
          "another": {
            "path": "some other value"
          }
        }
      ]
    }
  ]
}
```

Each grouping will end up in its own subdirectory in the zip that will be returned in the
multiple merge request.


## How to Run
### For Development
1. Install Maven
2. Install Docker
3. `$ mvn package && docker-compose up --build`

### For Production

There's currently no security/rate limiting. This is *currently*
meant to be deployed behind a firewall, with only trusted resources
able to make requests. *Will accept pull requests that integrate Spring
Security*. 


