#App Profile Creation Guide
###Summary of Document 
This document is to help users create App Profiles: XML files that can be parsed by JCAT to allow command-sending and telemetry-streaming for Apps running in the CFS. For additional help, look at CFE App Profiles for examples.
<br>

###Requirements for App Profiles:
######With respect to the CFS version of the App, everything in its App Profile is defined, accurate, and in correct order, including:
1. All <strong>&lt;commands&gt;&lt;command&gt;</strong>s.
2. All <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>s.
3. All <strong>&lt;inputparameter&gt;</strong>s and <strong>&lt;choiceparameter&gt;</strong>s within <strong>&lt;commands&gt;&lt;command&gt;&lt;parameters&gt;</strong>.

######Additionally, the App itself must abide by the following requirement:
1. The command code for each command must equal <strong>&lt;commandoffset&gt;</strong> + <strong>x</strong>, where <strong>x</strong> is the index of its App Profiles' matching <strong>&lt;command&gt;</strong> within <strong>&lt;commands&gt;</strong>.

<br>
###Basic-Depth App Profile Example:
	<?xml version="1.0" encoding="UTF-8"?>
	<channel>
		<name>[APPNAME]</name>
		<configs>
			[<config>
				<name>[CPU1]</name>
				<cmdmid>[0x1804]</cmdmid>
				<tlmmid>[0x0804]</tlmmid>
			</config>]
		</configs>
		<commandoffset>[+0]</commandoffset>
		<commands>[]</commands>
		<telemetry>[]</telemetry>
	</channel>

<strong>Notes: </strong>The above example meets the minimum requirements to define an App Profile. The parts contained in brackets are specific to the App, and would <strong>not</strong> be included in a real App Profile. They will be not be included in subsequent examples.
<br>
<br>

#####New Terms:
######Within <strong>&lt;channel&gt;</strong>:
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as the App's name in the GUI.</li>
	<li><strong>&lt;configs&gt;:</strong> The different possible <strong>&lt;config&gt;</strong>s that the App can run as.</li>
	<ul>
		<li><strong>&lt;config&gt;:</strong> A set of a <strong>&lt;cmdmid&gt;</strong>, <strong>&lt;tlmmid&gt;</strong>, and <strong>&lt;name&gt;</strong>. You can interact with more than one instance of the App by selecting a <strong>&lt;config&gt;</strong> for each desired instance at runtime.</li>
		<ul>
			<li><strong>&lt;name&gt;:</strong> The name to describe this <strong>&lt;config&gt;</strong> in the GUI.</li>
			<li><strong>&lt;cmdmid&gt;:</strong> The command message ID.
			<li><strong>&lt;tlmmid&gt;:</strong> The telemetry message ID.
		</ul>
	</ul>
	<li><strong>&lt;commandoffset&gt;:</strong> The lowest command code for the App (usually 0).</li>
	<li><strong>&lt;commands&gt;:</strong> The different <strong>&lt;command&gt;</strong>s that the App can execute.</li>
	<li><strong>&lt;telemetry&gt;:</strong> The different telemetry <strong>&lt;parameter&gt;</strong>s that the App's Housekeeping Packet outputs.</li>
</ul>
<br>
###Moderate-Depth App Profile Example (the above example, but expanded):
	<?xml version="1.0" encoding="UTF-8"?>
	<channel>
		<name>APPNAME</name>
		<configs>
			<config>
				<name>CPU1</name>
				<cmdmid>0x1804</cmdmid>
				<tlmmid>0x0804</tlmmid>
			</config>
		</configs>
		<commandoffset>+0</commandoffset>
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

<strong>Notes: </strong>The above example is <strong>not</strong> a correctly-formatted App Profile. This is because, and only because, the contents within <strong>&lt;choiceparameter&gt;</strong>, <strong>&lt;inputparameter&gt;</strong>, and <strong>&lt;spare&gt;</strong> are not defined.
<br>
<br>
#####New Terms:
######Within <strong>&lt;commands&gt;&lt;command&gt;</strong>: Defines a command that can be used on the App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as the <strong>&lt;command&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;parameters&gt;:</strong> The parameters required for the <strong>&lt;command&gt;</strong> to be sent. This can be absent if there are no parameters.</li>
	<ul>
		<li><strong>&lt;choiceparameter&gt;:</strong> The parameter type that requires the user to choose from a select list in drop-down form.</li>
		<li><strong>&lt;inputparameter&gt;:</strong> The parameter type that requires the user to manually enter the value in field.</li>
		<li><strong>&lt;spare&gt;:</strong> The parameter type that is invisible to the user, which contributes only byte values of zero to the command packet.</li>
	</ul>
</ul>

######Within <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>: Defines a telemetry parameter for the App sent by its Housekeeping Packet.
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as the <strong>&lt;parameter&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;type&gt;:</strong> The data type of the <strong>&lt;parameter&gt;</strong>. The only valid values are:</li>
	<ul>
		<li>int8, int16, int32</li>
		<li>uint8, uint16, uint32</li>
		<li>char</li>
	</ul>
	<li><strong>&lt;primitive&gt;:</strong> The primitive of the <strong>&lt;parameter&gt;</strong>. Can be either a string or integer. If defined as anything other than "string", or is missing entirely, it is considered an integer.</li>
	<li><strong>&lt;const&gt;:</strong> The variable whose value, as defined in the imported Constant Definition file at run-time, is multiplied by the byte value of the <strong>&lt;parameter&gt;</strong>'s <strong>&lt;type&gt;</strong> to determine the number of bytes assigned to this <strong>&lt;parameter&gt;</strong> in the command packet. Only works if the <strong>&lt;parameter&gt;</strong>'s <strong>&lt;primitive&gt;</strong> is a string.</li>
</ul>
<br>
