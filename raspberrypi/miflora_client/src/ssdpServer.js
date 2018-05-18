import Ssdp from '@achingbrain/ssdp';
import macaddress from 'macaddress';
import { promisify } from 'util';
import HTTP from './http';

const macaddressOne = promisify(macaddress.one);

export default class SSDPServer {
  static get USN() {
    return 'urn:schemas-upnp-org:device:SmarterMiFlora:1';
  }

  constructor() {
    this.server = Ssdp();
  }

  /**
   * The url of the of the descriptor file to advertise
   * @param url
   * @returns {Promise<*>}
   */
  async start(url) {
    const mac = await macaddressOne();
    const uri = `http://${HTTP.ADDRESS}:${HTTP.PORT}`;
    this.advertisement = await this.server.advertise({
      usn: SSDPServer.USN,
      location: {
        udp4: url,
      },
      details: {
        URLBase: uri,
        urls: {
          config: `${uri}/config`,
          status: `${uri}/status`,
        },
        device: {
          deviceType: SSDPServer.USN,
          friendlyName: 'Smarter Things MiFlora Server',
          manufacturer: '',
          manufacturerURL: '',
          modelDescription: '',
          modelName: 'SmarterThingsMiFlora',
          modelNumber: 'stmfv1',
          modelURL: '',
          serialNumber: mac,
          presentationURL: 'description.xml',
        },
      },
    });
    process.on('exit', () => this.advertisement.stop());
    return this.advertisement;
  }
}
