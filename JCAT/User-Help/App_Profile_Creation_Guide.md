#App Profile Creation Guide
###Summary of Document 
This document is to help users create App Profiles: XML files that can be parsed by JCAT to allow command-sending and telemetry-streaming for Apps running in the CFS. For additional help, look at cFE App Profiles for examples.
<br>

###Requirements for App Profiles:
######With respect to the version of the App running in the CFS, everything in its App Profile is defined, is accurate, and is in correct order, including:
1. All <strong>&lt;commands&gt;&lt;command&gt;</strong>s.
2. All <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>s.
3. All <strong>&lt;inputparameter&gt;</strong>s and <strong>&lt;choiceparameter&gt;</strong>s within all <strong>&lt;commands&gt;&lt;command&gt;&lt;parameters&gt;</strong>.

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
	<li><strong>&lt;name&gt;:</strong> The string to display as this App's name in the GUI.</li>
	<li><strong>&lt;configs&gt;:</strong> The different possible <strong>&lt;config&gt;</strong>s that this App can run as.</li>
	<ul>
		<li><strong>&lt;config&gt;:</strong> A set of a <strong>&lt;cmdmid&gt;</strong>, <strong>&lt;tlmmid&gt;</strong>, and <strong>&lt;name&gt;</strong>. You can interact with more than one instance of the this App in JCAT by selecting a <strong>&lt;config&gt;</strong> for each desired instance at run-time.</li>
		<ul>
			<li><strong>&lt;name&gt;:</strong> The name to describe this <strong>&lt;config&gt;</strong> in the GUI.</li>
			<li><strong>&lt;cmdmid&gt;:</strong> The command message ID.
			<li><strong>&lt;tlmmid&gt;:</strong> The telemetry message ID.
		</ul>
	</ul>
	<li><strong>&lt;commandoffset&gt;:</strong> The lowest command code for this App (usually 0).</li>
	<li><strong>&lt;commands&gt;:</strong> The different <strong>&lt;command&gt;</strong>s that this App can execute.</li>
	<li><strong>&lt;telemetry&gt;:</strong> The different telemetry <strong>&lt;parameter&gt;</strong>s that this App's Housekeeping Packet outputs.</li>
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
######Within <strong>&lt;commands&gt;&lt;command&gt;</strong>: Defines a command that can be sent to this App.
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as this <strong>&lt;command&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;parameters&gt;:</strong> The parameters required for this <strong>&lt;command&gt;</strong> to be sent. This can be absent if there are no parameters.</li>
	<ul>
		<li><strong>&lt;choiceparameter&gt;:</strong> The parameter type that requires the user to choose from a select list in drop-down form.</li>
		<li><strong>&lt;inputparameter&gt;:</strong> The parameter type that requires the user to manually enter the desired value in a field.</li>
		<li><strong>&lt;spare&gt;:</strong> The parameter type that is invisible to the user, but which contributes only byte values of zero to this <strong>&lt;command&gt;</strong>'s command packet.</li>
	</ul>
</ul>

######Within <strong>&lt;telemetry&gt;&lt;parameter&gt;</strong>: Defines a telemetry parameter contained in this App's Housekeeping Packet.
<ul>
	<li><strong>&lt;name&gt;:</strong> The string to display as this <strong>&lt;parameter&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;type&gt;:</strong> The data type of this <strong>&lt;parameter&gt;</strong>. The only valid values are:</li>
	<ul>
		<li>int8, int16, int32</li>
		<li>uint8, uint16, uint32</li>
		<li>char</li>
	</ul>
	<li><strong>&lt;primitive&gt;:</strong> The primitive of this <strong>&lt;parameter&gt;</strong>. Can be either a string or integer. If defined as anything other than "string", or is missing entirely, it is considered an integer.</li>
	<li><strong>&lt;const&gt;:</strong> The variable whose value, as defined in the imported Constant Definition file at run-time, is multiplied by the byte value of this <strong>&lt;parameter&gt;</strong>'s <strong>&lt;type&gt;</strong> to assign the number of bytes for this <strong>&lt;parameter&gt;</strong> in this command packet. Only considered if this <strong>&lt;parameter&gt;</strong>'s <strong>&lt;primitive&gt;</strong> is a string.</li>
</ul>
<br>
###In-Depth App Profile Example (the above example, but expanded):
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
					<choiceparameter>
						<name>ActiveTblFlag</name>
						<type>uint16</type>
						<choice>
							<name>CFE_TBL_INACTIVE_BUFFER</name>
							<value>0</value>
						</choice>
						<choice>
							<name>CFE_TBL_ACTIVE_BUFFER</name>
							<value>1</value>
						</choice>
					</choiceparameter>
					<inputparameter>
						<name>TableName</name>
						<type>char</type>
						<const>CFE_TBL_MAX_FULL_NAME_LEN</const>
						<primitive>string</primitive>
					</inputparameter>
					<spare>
						<type>uint8</type>
					</spare>
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
	
<strong>Notes: </strong>The above example <strong>is</strong> a correctly-formatted App Profile and will, as such, load into JCAT.
<br>
<br>
#####New Terms:
######Within <strong>&lt;choiceparameter&gt;</strong>:
<ul>
	<li><strong>&lt;name&gt;</strong>: The string to display as this <strong>&lt;choiceparameter&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;type&gt;</strong>: (Same as <strong>&lt;telemetry&gt;&lt;parameter&gt;&lt;type&gt;</strong>). <strong>Note: </strong>at this time, <strong>&lt;choiceparameter&gt;</strong>s only function correctly if they are <strong>&lt;type&gt;</strong> integer.</li>
	<li><strong>&lt;choice&gt;</strong>: A possible option for the user to select as this <strong>&lt;choiceparameter&gt;</strong>'s value in the GUI. A single <strong>&lt;choice&gt;</strong> must be selected for this command to send.</li>
	<ul>
		<li><strong>&lt;name&gt;</strong>: The string to display as this <strong>&lt;choice&gt;</strong>'s name in the GUI.</li>
		<li><strong>&lt;value&gt;</strong>: The integer value to set as this <strong>&lt;choiceparameter&gt;</strong>'s value if this <strong>&lt;choice&gt;</strong> is chosen and this <strong>&lt;command&gt;</strong> is sent.</li>
	</ul>
</ul>
######Within <strong>&lt;inputparameter&gt;</strong>:
<ul>
	<li><strong>&lt;name&gt;</strong>: The string to display as this <strong>&lt;inputparameter&gt;</strong>'s name in the GUI.</li>
	<li><strong>&lt;type&gt;</strong>: (Same as <strong>&lt;telemetry&gt;&lt;parameter&gt;&lt;type&gt;</strong>).</li>
	<li><strong>&lt;const&gt;</strong>: (Same as <strong>&lt;telemetry&gt;&lt;parameter&gt;&lt;const&gt;</strong>).</li>
	<li><strong>&lt;primitive&gt;</strong>: (Same as <strong>&lt;telemetry&gt;&lt;parameter&gt;&lt;primitive&gt;</strong>).</li>
</ul>
######Within <strong>&lt;spare&gt;</strong>:
<ul>
	<li><strong>&lt;type&gt;</strong>: (Same as <strong>&lt;telemetry&gt;&lt;parameter&gt;&lt;type&gt;</strong>). Note: for a <strong>&lt;spare&gt;</strong>, the <strong>&lt;type&gt;</strong> is only used to denote the amount of bytes to allocate to it.</li>
</ul>
