/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

$.generateId = function() {
    return arguments.callee.prefix + arguments.callee.count++;
};
$.generateId.prefix = 'jq$';
$.generateId.count = 0;
$.fn.generateId = function() {
    return this.each(function() {
        this.id = $.generateId();
    });
};


/*
 * @author: igor
 */
 
(function($){

	/** forwards function invocation to fullCalendar plugin */
	function forward(element, args) {
		return $.fn.fullCalendar.apply($(element), args);
	}

	function copyArray(original) {
		var copy=[];
		for (var i=0,len=original.length;i<len;i++)
			copy.push(original[i]);
		return copy;
	}
	
    $.fn.fullCalendarExt = function(method){
    
    	var _arguments=copyArray(arguments);
	  	if (typeof method == 'string') {
			var args = Array.prototype.slice.call(_arguments, 1);
			var res;
			this.each(function() {
				var instance = $.data(this, 'fullCalendarExt');
				if (instance && $.isFunction(instance[method])) {
					
					var r = instance[method].apply(instance, args);
					if (res === undefined) {
						res = r;
					}
					if (method == 'destroy') {
						$.removeData(this, 'fullCalendarExt');
						forward(this, _arguments);
					}
				} else {
					res=forward(this, _arguments);
				}
			});
			if (res !== undefined) {
				return res;
			}
			return this;
		}
		

		this.each(function() {
			
			var ext=(function(owner) {
				var options=method;
				
				
				var sources=options.eventSources||[];
				sources=copyArray(sources);
				
				$(sources).each(function() {
					if (typeof(this.data.fcxEnabled)==="undefined") {
						this.data.fcxEnabled=true;
					}
				});
				
				
				function findSource(uuid) {
					for (var i=0,len=sources.length;i<len;i++) {
						if (sources[i].data.fcxUuid===uuid) return sources[i];
					}
				}
				
				function _toggleSource(owner, uuid, val) {
					var source=findSource(uuid);
					val = (typeof(val) === "undefined") ? (!source.data.fcxEnabled) : val;
					if (val&&!source.data.fcxEnabled) {
						$(owner).fullCalendar('addEventSource', source);
						source.data.fcxEnabled=true;
					} else if (!val&&source.data.fcxEnabled) {
						$(owner).fullCalendar('removeEventSource', source);
						source.data.fcxEnabled=false;
					}
				}
				
				
				var ext={};
			
				ext.createEventSourceSelector=function(id) {
					var ul=$("#"+id);
					$(sources).each(function() {
						if (this.includeInSelector)
						{
							var sourceUuid=this.data.fcxUuid||null;
							var checkbox=$("<input type='checkbox'/>");
							if (this.data.fcxEnabled) { checkbox.attr("checked","checked"); }
							if (!this.enableInSelector) { checkbox.attr("disabled", "true"); }
							checkbox.bind("click", function() { _toggleSource(owner, sourceUuid, this.checked); });
							checkbox.generateId();
							// Style current eventSource selector similar to the events in the calendar (if no specific CSS class is provided)
							var colorStyle = "";
							if (this.backgroundColor) {
								colorStyle += "background-color: " + this.backgroundColor + ";";
								if (this.textColor) {
									colorStyle += "color: " + this.textColor + ";";
								}
							}
							var li=$("<li></li>");
							// Add the class used by the event source to the <li> so it can be styled.
							if (this.className && $.trim(this.className) != "") { li.attr("class",this.className); }
							checkbox.appendTo(li);
							$("<label for='" + checkbox.attr("id") + "' style=\"" + colorStyle + "\">" + this.data.fcxTitle + "</label>")
								.appendTo(li);
							li.appendTo(ul);
						}
					});
				};
				
				ext.toggleSource=function(sourceId, enabled) {
					_toggleSource(owner, sourceId, enabled); 
				};
				
				return ext;
			}(this));
			
			$.data(this, 'fullCalendarExt', ext);
			
			forward(this, _arguments);
		});
    };

	
})(jQuery);

function fullCalendarExtIsoDate(d) {
	function pad(n){return n<10 ? '0'+n:n;}
	return d.getUTCFullYear()+'-'+pad(d.getUTCMonth()+1)+'-'+pad(d.getUTCDate())+'T'
		+pad(d.getUTCHours())+':'+pad(d.getUTCMinutes())+':'+pad(d.getUTCSeconds())+'Z';
}
