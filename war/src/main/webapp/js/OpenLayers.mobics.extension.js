/**
 * @requires OpenLayers/Control.js
 * @requires OpenLayers/Events/buttonclick.js
 */

/**
 * Class: OpenLayers.Control.Legend
 * The Legend control is a link for show / hide legend.
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.Legend = OpenLayers.Class(OpenLayers.Control, {

    legendId: "olLegendLink",

    /**
     * Method: draw
     *
     * Returns:
     * {DOMElement} A reference to the DOMElement containing the legend link.
     */
    draw: function() {
        var div = OpenLayers.Control.prototype.draw.apply(this),
            legend = this.getOrCreateLinks(div),
            eventsInstance = this.map.events;
        
        eventsInstance.register("buttonclick", this, this.onLegendClick);
        
        this.legendLink = legend;
        return div;
    },
    
    /**
     * Method: getOrCreateLinks
     * 
     * Parameters:
     * el - {DOMElement}
     *
     * Return: 
     * {Object} Object with legend properties referencing links.
     */
    getOrCreateLinks: function(el) {
        var legend = document.getElementById(this.legendId);
        if (!legend) {
            legend = document.createElement("a");
            legend.href = "#legend";
            legend.className = "olControlLegend";
            el.appendChild(legend);
        }
        OpenLayers.Element.addClass(legend, "olButton");
        return {
            legend: legend
        };
    },
    
    /**
     * Method: onLegendClick
     * Called when legend link is clicked.
     */
    onLegendClick: function() {},

    /** 
     * Method: destroy
     * Clean up.
     */
    destroy: function() {
        if (this.map) {
            this.map.events.unregister("buttonclick", this, this.onLegendClick);
        }
        delete this.legendLink;
        OpenLayers.Control.prototype.destroy.apply(this);
    },

    CLASS_NAME: "OpenLayers.Control.Legend"
});

/**
 * Class: OpenLayers.Control.List
 * The List control is a link for the car list.
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.List = OpenLayers.Class(OpenLayers.Control, {
    
    listText: "",

    listId: "olListLink",

    /**
     * Method: draw
     *
     * Returns:
     * {DOMElement} A reference to the DOMElement containing the list link.
     */
    draw: function() {
        var div = OpenLayers.Control.prototype.draw.apply(this),
            list = this.getOrCreateLinks(div),
            eventsInstance = this.map.events;
        
        eventsInstance.register("buttonclick", this, this.onListClick);
        
        this.listLink = list;
        return div;
    },
    
    /**
     * Method: getOrCreateLinks
     * 
     * Parameters:
     * el - {DOMElement}
     *
     * Return: 
     * {Object} Object with list properties referencing link.
     */
    getOrCreateLinks: function(el) {
        var list = document.getElementById(this.listId);
        if (!list) {
            list = document.createElement("a");
            list.href = "#list";
            list.appendChild(document.createTextNode(this.listText));
            list.className = "olControlList";
            el.appendChild(list);
        }
        OpenLayers.Element.addClass(list, "olButton");
        return {
            list: list
        };
    },
    
    /**
     * Method: onListClick
     * Called when list link is clicked.
     */
    onListClick: function() {},

    /** 
     * Method: destroy
     * Clean up.
     */
    destroy: function() {
        if (this.map) {
            this.map.events.unregister("buttonclick", this, this.onListClick);
        }
        delete this.listLink;
        OpenLayers.Control.prototype.destroy.apply(this);
    },

    CLASS_NAME: "OpenLayers.Control.List"
});

/**
 * Class: OpenLayers.Control.MyLocation
 * The MyLocation control is a link for the car list.
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.MyLocation = OpenLayers.Class(OpenLayers.Control, {
    
    myLocationId: "olMyLocationLink",

    /**
     * Method: draw
     *
     * Returns:
     * {DOMElement} A reference to the DOMElement containing the list link.
     */
    draw: function() {
        var div = OpenLayers.Control.prototype.draw.apply(this),
            myLocation = this.getOrCreateLinks(div),
            eventsInstance = this.map.events;
        
        eventsInstance.register("buttonclick", this, this.onMyLocationClick);
        
        this.myLocationLink = myLocation;
        return div;
    },
    
    /**
     * Method: getOrCreateLinks
     * 
     * Parameters:
     * el - {DOMElement}
     *
     * Return: 
     * {Object} Object with list properties referencing link.
     */
    getOrCreateLinks: function(el) {
        var myLocation = document.getElementById(this.myLocationId);
        if (!myLocation) {
            myLocation = document.createElement("a");
            myLocation.href = "#myLocation";
            myLocation.className = "olControlMyLocation";
            el.appendChild(myLocation);
        }
        OpenLayers.Element.addClass(myLocation, "olButton");
        return {
            myLocation: myLocation
        };
    },
    
    /**
     * Method: onMyLocationClick
     * Called when myLocation link is clicked.
     */
    onMyLocationClick: function() {},

    /** 
     * Method: destroy
     * Clean up.
     */
    destroy: function() {
        if (this.map) {
            this.map.events.unregister("buttonclick", this, this.onMyLocationClick);
        }
        delete this.myLocationLink;
        OpenLayers.Control.prototype.destroy.apply(this);
    },

    CLASS_NAME: "OpenLayers.Control.MyLocation"
});


/**
 * Class: OpenLayers.Control.Gas
 * The List control is a link for the Gas station list.
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Control.Stations = OpenLayers.Class(OpenLayers.Control, {

    stationsId: "olStationsLink",

    /**
     * Method: draw
     *
     * Returns:
     * {DOMElement} A reference to the DOMElement containing the list link.
     */
    draw: function() {
        var div = OpenLayers.Control.prototype.draw.apply(this),
            stations = this.getOrCreateLinks(div),
            eventsInstance = this.map.events;

        eventsInstance.register("buttonclick", this, this.onStationsClick);

        this.stationsLink = stations;
        return div;
    },

    /**
     * Method: getOrCreateLinks
     *
     * Parameters:
     * el - {DOMElement}
     *
     * Return:
     * {Object} Object with list properties referencing link.
     */
    getOrCreateLinks: function(el) {
        var stations = document.getElementById(this.stationsId);
        if (!stations) {
            stations = document.createElement("a");
            stations.href = "#stations";
            stations.className = "olControlStations";
            el.appendChild(stations);
        }
        OpenLayers.Element.addClass(stations, "olButton");
        return {
            stations: stations
        };
    },

    /**
     * Method: onStationsClick
     * Called when list link is clicked.
     */
    onStationsClick: function() {alert('1');},

    /**
     * Method: destroy
     * Clean up.
     */
    destroy: function() {
        if (this.map) {
            this.map.events.unregister("buttonclick", this, this.onListClick);
        }
        delete this.stationsLink;
        OpenLayers.Control.prototype.destroy.apply(this);
    },

    CLASS_NAME: "OpenLayers.Control.Stations"
});
