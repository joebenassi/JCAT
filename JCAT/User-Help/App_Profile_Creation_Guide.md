#Requirements/Assumptions for the XML:
###Everything for the App is defined, and everything is in correct order, including
1. All <commands><command>s for the App
2. All <telemetry><parameter>s for the App
3. All <inputparameter>s and <choiceparameter>s in <commands><command><parameters> for the App

###The App’s commands must abide by the following requirement:
1. The command code for each command must be equal to <commandoffset> + X, where X is the index of the applicable <command> within <commands>


###Basic Format Example (not a profile for any current App):
<?xml version="1.0" encoding="UTF-8"?>
<channel>
	<name>[APPNAME]</name>
	<configs>
		<config>
			<name>[CPU 1]</name>
			<cmdmid>[0x1804]</cmdmid>
			<tlmmid>[0x0804]</tlmmid>
		</config>
	</configs>
	<commandoffset>[+0]</commandoffset>
	<commands></commands>
	<telemetry></telemetry>
</channel>
