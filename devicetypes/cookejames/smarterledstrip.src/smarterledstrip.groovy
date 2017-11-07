/**
 *  Smart Led Strip
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Author: James Cooke based on work by Allan (vseven)
 *  Date: 2017-11-06
 */

import groovy.json.JsonSlurper

// for the UI
metadata {
	definition (name: "SmarterLedStrip", namespace: "cookejames", author: "James Cooke - based on work by Allan (vseven)") {
	capability "Switch"
	capability "Switch Level"
	capability "Actuator"
	capability "Color Control"
	capability "Sensor"
	capability "Light"
  capability "Refresh"
  capability "Health Check"

	command "generateEvent", ["string", "string"]
	command "softwhite"
	command "daylight"
	command "warmwhite"
	command "red"
	command "green"
	command "blue"
	command "cyan"
	command "magenta"
	command "orange"
	command "purple"
	command "yellow"
	command "white"
  command "reboot"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2){
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.illuminance.illuminance.dark", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.illuminance.illuminance.light", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.illuminance.illuminance.light", backgroundColor:"#ffffff", nextState:"turningOn"
			}
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
        			attributeState "level", action:"switch level.setLevel"
    			}
			tileAttribute ("device.color", key: "COLOR_CONTROL") {
				attributeState "color", action:"color control.setColor"
			}
		}
		standardTile("softwhite", "device.softwhite", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offsoftwhite", label:"soft white", action:"softwhite", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onsoftwhite", label:"soft white", action:"softwhite", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFF1E0"
		}
		standardTile("daylight", "device.daylight", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offdaylight", label:"daylight", action:"daylight", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "ondaylight", label:"daylight", action:"daylight", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFFFB"
		}
		standardTile("warmwhite", "device.warmwhite", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offwarmwhite", label:"warm white", action:"warmwhite", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onwarmwhite", label:"warm white", action:"warmwhite", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFF4E5"
		}
		standardTile("red", "device.red", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offred", label:"red", action:"red", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onred", label:"red", action:"red", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF0000"
		}
		standardTile("green", "device.green", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offgreen", label:"green", action:"green", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "ongreen", label:"green", action:"green", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00FF00"
		}
		standardTile("blue", "device.blue", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offblue", label:"blue", action:"blue", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onblue", label:"blue", action:"blue", icon:"st.illuminance.illuminance.bright", backgroundColor:"#0000FF"
		}
		standardTile("cyan", "device.cyan", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offcyan", label:"cyan", action:"cyan", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "oncyan", label:"cyan", action:"cyan", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00FFFF"
		}
		standardTile("magenta", "device.magenta", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offmagenta", label:"magenta", action:"magenta", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onmagenta", label:"magenta", action:"magenta", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF00FF"
		}
		standardTile("orange", "device.orange", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offorange", label:"orange", action:"orange", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onorange", label:"orange", action:"orange", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF6600"
		}
		standardTile("purple", "device.purple", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offpurple", label:"purple", action:"purple", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onpurple", label:"purple", action:"purple", icon:"st.illuminance.illuminance.bright", backgroundColor:"#BF00FF"
		}
		standardTile("yellow", "device.yellow", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offyellow", label:"yellow", action:"yellow", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onyellow", label:"yellow", action:"yellow", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFF00"
		}
		standardTile("white", "device.white", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offwhite", label:"White", action:"white", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onwhite", label:"White", action:"white", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFFFF"
		}
    valueTile("ip", "ip", width: 3, height: 2) {
        state "ip", label:'IP Address\r\n${currentValue}'
    }
    valueTile("lastUpdated", "device.lastUpdated", inactiveLabel: false, decoration: "flat", width: 3, height: 2) {
        state "default", label:'Last Updated ${currentValue}', backgroundColor:"#ffffff"
    }
		main(["switch"])
		details(["switch", "level", "color", "softwhite","daylight","warmwhite","red","green","blue","white","cyan",
			 "magenta","orange","purple","yellow","ip","lastUpdated"])
	}
}

def on() {
    sendEvent(name: "switch", value: "on")
    def lastColor = device.latestValue("color")
    log.debug("On pressed.  Sending last known color value of $lastColor or if null command to white.")
    if ( lastColor == Null ) {  // For initial run
    	return white()
    } else {
    	return adjustColor(lastColor)
    }
}

def off() {
    log.debug("Off pressed.")
    toggleTiles("off")
    sendEvent(name: "switch", value: "off")
    return getAction("/off")
}

def setColor(value) {
    toggleTiles("off") //turn off the hard color tiles
    sendEvent(name: "color", value: value.hex)
    // Since the color selector takes into account lightness we have to reconvert the HEX and adjust the slider
    def colorRGB = hexToRgb(value.hex)
    def colorHSL = rgbToHSL(colorRGB)
    def myLightness = colorHSL.l * 100
    // log.debug("Lightness: $myLightness")
    sendEvent(name: "level", value: myLightness)
    adjustColor(value.hex)
}

def setLevel(value) {
    def level = Math.min(value as Integer, 100)
    // log.debug("Level value in percentage: $level")
    sendEvent(name: "level", value: level)

    // Turn on or off based on level selection
    if (level == 0) {
	off()
    } else {
	if (device.latestValue("switch") == "off") { on() }
	def lastColor = device.latestValue("color")
	    // log.debug("lastColor value is $lastColor")
	adjustColor(lastColor)
    }
}

def adjustColor(colorInHEX) {
    // Convert the hex color, apply the level after making sure its valid, then send to device
    //log.debug("colorInHEX passed in: $colorInHEX")
    def level = device.latestValue("level")
    // log.debug("level value is $level")
    if(level == null)
    	level = 50
    log.debug "level is: ${level}"

    def c = hexToRgb(colorInHEX)

    def r = c.r * (level/100)
    def g = c.g * (level/100)
    def b = c.b * (level/100)

    def adjustedColor = "r:${r} g:${g} b:${b}"
    log.debug("Adjusted color is $adjustedColor")

    return getAction("/on?red=${r}&green=${g}&blue=${b}")
}

def sendLastUpdated() {
    // Update lastUpdated date and time
    def nowDay = new Date().format("MMM dd", location.timeZone)
    def nowTime = new Date().format("h:mm a", location.timeZone)
    sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)
}

def doColorButton(colorName) {
    //log.debug "doColorButton: '${colorName}()'"
    toggleTiles(colorName.toLowerCase().replaceAll("\\s",""))
    def colorButtonHEX = getColorData(colorName)

    // Update the devices color for the button.
    sendEvent(name: "color", value: colorButtonHEX)
    adjustColor(colorButtonHEX)
}

def getColorData(colorName) {
    //log.debug "getColorData: ${colorName}"
    def colorRGB = colorNameToRgb(colorName)
    def colorHex = rgbToHex(colorRGB)

    colorHex
}

private hex(value, width=2) {
    def s = new BigInteger(Math.round(value).toString()).toString(16)
    while (s.size() < width) {
	s = "0" + s
    }
    s
}

def hexToRgb(colorHex) {
    //log.debug("passed in colorHex: $colorHex")
    def rrInt = Integer.parseInt(colorHex.substring(1,3),16)
    def ggInt = Integer.parseInt(colorHex.substring(3,5),16)
    def bbInt = Integer.parseInt(colorHex.substring(5,7),16)

    def colorData = [:]
    colorData = [r: rrInt, g: ggInt, b: bbInt]

    colorData
}

def rgbToHex(rgb) {
    def r = hex(rgb.r)
    def g = hex(rgb.g)
    def b = hex(rgb.b)

    def hexColor = "#${r}${g}${b}"

    hexColor
}

def rgbToHSL(rgb) {
	def r = rgb.r / 255
    def g = rgb.g / 255
    def b = rgb.b / 255
    def h = 0
    def s = 0
    def l = 0

    def var_min = [r,g,b].min()
    def var_max = [r,g,b].max()
    def del_max = var_max - var_min

    l = (var_max + var_min) / 2

    if (del_max == 0) {
            h = 0
            s = 0
    } else {
    	if (l < 0.5) { s = del_max / (var_max + var_min) }
        else { s = del_max / (2 - var_max - var_min) }

        def del_r = (((var_max - r) / 6) + (del_max / 2)) / del_max
        def del_g = (((var_max - g) / 6) + (del_max / 2)) / del_max
        def del_b = (((var_max - b) / 6) + (del_max / 2)) / del_max

        if (r == var_max) { h = del_b - del_g }
        else if (g == var_max) { h = (1 / 3) + del_r - del_b }
        else if (b == var_max) { h = (2 / 3) + del_g - del_r }

		if (h < 0) { h += 1 }
        if (h > 1) { h -= 1 }
    }
    def hsl = [:]
    hsl = [h: h * 100, s: s * 100, l: l]

    hsl
}

def colorNameToRgb(color) {
    final colors = [
	[name:"Soft White",	r: 255, g: 241, b: 224	],
	[name:"Daylight", 	r: 255, g: 255, b: 251	],
	[name:"Warm White", 	r: 255, g: 244, b: 229	],

	[name:"Red", 		r: 255, g: 0,	b: 0	],
	[name:"Green", 		r: 0, 	g: 255,	b: 0	],
	[name:"Blue", 		r: 0, 	g: 0,	b: 255	],

	[name:"Cyan", 		r: 0, 	g: 255,	b: 255	],
	[name:"Magenta", 	r: 255, g: 0,	b: 33	],
	[name:"Orange", 	r: 255, g: 102, b: 0	],

	[name:"Purple", 	r: 170, g: 0,	b: 255	],
	[name:"Yellow", 	r: 255, g: 255, b: 0	],
	[name:"White", 		r: 255, g: 255, b: 255	]
    ]
    def colorData = [:]
    colorData = colors.find { it.name == color }
    colorData
}

def toggleTiles(color) {
    state.colorTiles = []
    if ( !state.colorTiles ) {
    	state.colorTiles = ["softwhite","daylight","warmwhite","red","green","blue","cyan","magenta",
			    "orange","purple","yellow","white"]
    }

    def cmds = []

    state.colorTiles.each({
    	if ( it == color ) {
            log.debug "Turning ${it} on"
            cmds << sendEvent(name: it, value: "on${it}", displayed: True, descriptionText: "${device.displayName} ${color} is 'ON'", isStateChange: true)
        } else {
            //log.debug "Turning ${it} off"
            cmds << sendEvent(name: it, value: "off${it}", displayed: false)
      }
    })
}

// rows of buttons
def softwhite() { doColorButton("Soft White") }
def daylight()  { doColorButton("Daylight") }
def warmwhite() { doColorButton("Warm White") }

def red() 	{ doColorButton("Red") }
def green() 	{ doColorButton("Green") }
def blue() 	{ doColorButton("Blue") }

def cyan() 	{ doColorButton("Cyan") }
def magenta()	{ doColorButton("Magenta") }
def orange() 	{ doColorButton("Orange") }

def purple()	{ doColorButton("Purple") }
def yellow() 	{ doColorButton("Yellow") }
def white() 	{ doColorButton("White") }

// Additions
private getAction(uri){
  updateDNI()

  def userpass

  if(password != null && password != "")
    userpass = encodeCredentials("admin", password)

  def headers = getHeader(userpass)

  def hubAction = new physicalgraph.device.HubAction([
    method: "GET",
    path: uri,
    headers: headers
  ])
  return hubAction
}

def installed() {
	log.debug "installed()"
	configure()
}

def configure() {
    logging("configure()", 1)
    def cmds = []
    // cmds = update_needed_settings()
    if (cmds != []) cmds
}

def updated()
{
    logging("updated()", 1)
    def cmds = []
    // cmds = update_needed_settings()
    sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "lan", hubHardwareId: device.hub.hardwareID])
    sendEvent(name:"needUpdate", value: device.currentValue("needUpdate"), displayed:false, isStateChange: true)
    if (cmds != []) response(cmds)
}

private def logging(message, level) {
    if (logLevel != "0"){
    switch (logLevel) {
       case "1":
          if (level > 1)
             log.debug "$message"
       break
       case "99":
          log.debug "$message"
       break
    }
    }
}

def parse(description) {
	log.debug "Parsing: ${description}"
  def events = []
  def descMap = parseDescriptionAsMap(description)
  def body
  //log.debug "descMap: ${descMap}"

  if (!state.mac || state.mac != descMap["mac"]) {
	   log.debug "Mac address of device found ${descMap["mac"]}"
     updateDataValue("mac", descMap["mac"])
	}

  if (state.mac != null && state.dni != state.mac) state.dni = setDeviceNetworkId(state.mac)
  if (descMap["body"]) body = new String(descMap["body"].decodeBase64())

  if (body && body != "") {
    //log.debug body
    if(body.startsWith("{") || body.startsWith("[")) {
      def slurper = new JsonSlurper()
      def result = slurper.parseText(body)

      log.debug "result: ${result}"

      if (result.containsKey("status")) {
          events << createEvent(name: "switch", value: result.status)
      }
      if (result.containsKey("uptime")) {
          events << createEvent(name: 'uptime', value: result.uptime, displayed: false)
      }
    }

    if (!device.currentValue("ip") || (device.currentValue("ip") != getDataValue("ip"))) {
      events << createEvent(name: 'ip', value: getDataValue("ip"))
    }

    sendLastUpdated();

    return events
  }
}

def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
    if (nameAndValue.length == 2) map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    else map += [(nameAndValue[0].trim()):""]
	}
}

def refresh() {
	log.debug "refresh()"
  def cmds = []
  cmds << getAction("/status")
  return cmds
}

def ping() {
  log.debug "ping()"
  refresh()
}

private setDeviceNetworkId(ip, port = null){
    def myDNI
    if (port == null) {
        myDNI = ip
    } else {
  	    def iphex = convertIPtoHex(ip)
  	    def porthex = convertPortToHex(port)
        myDNI = "$iphex:$porthex"
    }
    log.debug "Device Network Id set to ${myDNI}"
    return myDNI
}

private updateDNI() {
    if (state.dni != null && state.dni != "" && device.deviceNetworkId != state.dni) {
       device.deviceNetworkId = state.dni
    }
}

private getHostAddress() {
    if (override == "true" && ip != null && ip != ""){
        return "${ip}:80"
    }
    else if(getDeviceDataByName("ip") && getDeviceDataByName("port")){
        return "${getDeviceDataByName("ip")}:${getDeviceDataByName("port")}"
    }else{
	    return "${ip}:80"
    }
}

private String convertIPtoHex(ipAddress) {
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hex
}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
  return hexport
}


private encodeCredentials(username, password){
	def userpassascii = "${username}:${password}"
  def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
  return userpass
}

private getHeader(userpass = null){
    def headers = [:]
    headers.put("Host", getHostAddress())
    headers.put("Content-Type", "application/x-www-form-urlencoded")
    if (userpass != null)
       headers.put("Authorization", userpass)
    return headers
}

def reboot() {
	log.debug "reboot()"
  def uri = "/reboot"
  getAction(uri)
}

def sync(ip, port) {
    def existingIp = getDataValue("ip")
    def existingPort = getDataValue("port")
    if (ip && ip != existingIp) {
        updateDataValue("ip", ip)
        sendEvent(name: 'ip', value: ip)
    }
    if (port && port != existingPort) {
        updateDataValue("port", port)
    }
}
