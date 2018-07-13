# Docmm4j

Document Mail Merge For Java (and Spring Boot).

This repository contains a super simple microservice which performs
mail merge operations for Word Documents that contain merge fields.

## Example Use

1. Add a Word Document to the document repository by `POST`ing to
   `http://your-url-here/documents`. This will return the location
   of the resource.

```json
{
    "formNumber": "SampleForm1",
    "mapping": {
        "MergeFieldName": "$.json.path",
        "MergeField2Name": "$.another.path"
    }
}
```

2. Upload the template that contains merge fields by `POST`ing the
   document to `http://your-url-here/documents/{formNumber}/template`
   as a multipart form file.
3. Perform merge operations by `POST`ing to the
   `http://your-url-here/merge`. The word document will be returned
   in the response body if successful.

```json
{
    "formNumber": "SampleForm1",
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

## Limitations

- Only does one document at a time.
- More metadata storage needed for reports.
- No batch support (yet).

## Running

1. Install Maven
2. Install Docker
3. `$ mvn package && docker-compose up`



