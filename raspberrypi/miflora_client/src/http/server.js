import express from 'express';
import bodyParser from 'body-parser';

const LISTEN_PORT = 8080;

export default class HTTPServer {
  static get LISTEN_PORT() {
    return 8080;
  }
  constructor() {
    this.server = express();
    this.server.use(bodyParser.json())
  }

  start() {
    this.server.listen(LISTEN_PORT, (error) => {
      if (error) {
        console.error(error);
        return process.exit(1);
      }
      return console.log('Started HTTP server');
    });
  }

  get(...args) {
    return this.server.get(...args);
  }

  post(...args) {
    return this.server.post(...args);
  }
}
