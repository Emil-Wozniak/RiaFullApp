import service from "./Service";

export class FileService {
  getFileFromServer(filename) {
    //returns Promise object
    return service
      .getRestClient()
      .get(filename, { responseType: "blob" });
  }
}
