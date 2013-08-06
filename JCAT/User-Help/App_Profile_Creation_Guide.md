#App Profile Creation Guide
###Summary of Document 
This document is to help users create App Profiles: XML files that can be parsed by JCAT to allow command-sending and telemetry-streaming for an App running on the CFS. For additional help, look at CFE App Profiles for examples.
<br>

###Requirements for App Profiles:
######With respect to the CFS version of the App, everything in the App Profile is defined and in correct order, including:
1. All <strong>&lt;commands&gt;&lt;command&gt;</strong>s
2. All <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>s
3. All <strong>&lt;inputparameter&gt;</strong>s and <strong>&lt;choiceparameter&gt;</strong>s within <strong>&lt;commands&gt;&lt;command&gt;&lt;parameters&gt;</strong>

######Additionally, the App itself must abide by the following requirement:
1. The command code for each command within the CFS App must equal <strong>&lt;commandoffset&gt;</strong> + <strong>x</strong>, where <strong>x</strong> is the index of the matching <strong>&lt;command&gt;</strong> within <strong>&lt;commands&gt;</strong> in its App Profile

<br>
###Basic App Profile Example (not a current App Profile):
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

<strong>Notes: </strong>The above example meets the minimum requirements to define an App Profile. The parts contained in brackets are specific to the App.
<br>
<br>

#####New Terms:
######Within <strong>&lt;channel&gt;</strong>:
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as the App's name in the GUI</li>
	<li><strong>&lt;configs&gt;:</strong> The different possible <strong>&lt;config&gt;</strong>s that the App can run as</li>
	<li><strong>&lt;config&gt;:</strong> A set of a <strong>&lt;cmdmid&gt;</strong> and <strong>&lt;tlmmid&gt;</strong>, with a <strong>&lt;name&gt;</strong> to describe it in the GUI. You can interact with more than one instance of the App by selecting a <strong>&lt;config&gt;</strong> for each desired instance at runtime</li>
	<li><strong>&lt;commandoffset&gt;:</strong> The lowest command code for the App (usually 0)</li>
	<li><strong>&lt;commands&gt;:</strong> The different <strong>&lt;command&gt;</strong>s that the App can execute</li>
	<li><strong>&lt;telemetry&gt;:</strong> The different telemetry <strong>&lt;parameters&gt;</strong>s that the App's Housekeeping Packet displays</li>
</ul>
<br>
###Moderate-Depth App Profile Example (the above example, but expanded):
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
		<commandoffset>[+0]</commandoffset&gt;
		<commands>
			<command>
				<name>CFE_TBL_VALIDATE_CC</name>
				<parameters> 					
					<choiceparameter></choiceparameter>
					<inputparameter></inputparameter>
					<spare></spare>
				</parameters>
			</command>
		</commands>
		<telemetry>
			<parameter>
				<name>LastFileDumped</name>
				<type>char</type>
				<primitive>string</primitive>
				<const>OS_MAX_PATH_LEN</const>
			</parameter>
			<parameter>
				<name>ByteAlignPad2</name>
				<type>uint16</type>
			</parameter>	
		</telemetry>
	</channel>

<strong>Notes: </strong>The above example is <strong>not</strong> a correctly-formatted App Profile. This is because, and only because, the contents within <strong>&lt;choiceparameter&gt;, &lt;inputparameter&gt;, and &lt;spare&gt; are not defined;
<br>
#####New Terms:
######Within <strong>&lt;commands&gt;&lt;command&gt;</strong>: Defines a command that can be used on the App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The name of the command.</li>
	<li><strong>&lt;parameters&gt;:</strong> The parameters for the command. This can be absent if there are no parameters for the command.</li>
	<ul>
		<li><strong>&lt;choiceparameter&gt;:</strong> A parameter where the user chooses from predetermined values in drop-down form.</li>
		<li><strong>&lt;inputparameter&gt;:</strong> A parameter where the user manually types in the value.</li>
		<li><strong>&lt;spare&gt;:</strong> A parameter that is invisible to the user, which contributes only byte values of zero to the packet.</li>
	</ul>
</ul>
<br>

######Within <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>: Defines an aspect of telemetry about the App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The name of the parameter to display.
	<li><strong>&lt;type&gt;:</strong> The data type of the parameter. The only valid values are:
	<ul>
		<li>int8, int16, int32</li>
		<li>uint8, uint16, uint32</li>
		<li>char</li>
	</ul>
	<li><strong>&lt;primitive&gt;:</strong> Defines whether or not the parameter is a string. If it is defined as “<primitive>string</primitive>”, it would be a string. If not, it is assumed an integer. If <primitive> is missing entirely, it is assumed an integer.</li>
	<li><strong>&lt;const&gt;:</strong> A variable whose value, as defined in the Constant Definition file, is the length of the array of <type>s in the string. This is used for command byte packing.</li>
</ul>
<br>
	
	
	
	
