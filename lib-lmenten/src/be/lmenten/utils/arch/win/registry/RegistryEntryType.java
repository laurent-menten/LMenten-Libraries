package be.lmenten.utils.arch.win.registry;

public enum RegistryEntryType
{
	REG_NONE,							// No type (the stored value, if any)

	REG_SZ,								// A string value, normally stored and exposed in UTF-16LE
										// (when using the Unicode version of Win32 API functions),
										// usually terminated by a NUL character

	REG_EXPAND_SZ,						// An "expandable" string value that can contain environment
										// variables, normally stored and exposed in UTF-16LE, usually
										// terminated by a NUL character

	REG_BINARY,							// Binary data (any arbitrary data)

	REG_DWORD,							// A DWORD value, a 32-bit unsigned integer (numbers between 0
										// and 4,294,967,295 [232 – 1]) (little-endian)
	REG_DWORD_LITTLE_ENDIAN,			// 	

	REG_DWORD_BIG_ENDIAN,				// A DWORD value, a 32-bit unsigned integer (numbers between 0
										// and 4,294,967,295 [232 – 1]) (big-endian)

	REG_LINK,							// A symbolic link (UNICODE) to another registry key, specifying
										// a root key and the path to the target key

	REG_MULTI_SZ,						// A multi-string value, which is an ordered list of non-empty
										// strings, normally stored and exposed in UTF-16LE, each one
										// terminated by a NUL character, the list being normally terminated
										// by a second NUL character.

	REG_RESOURCE_LIST,					// A resource list (used by the Plug-n-Play hardware enumeration
										// and configuration)

	REG_FULL_RESOURCE_DESCRIPTOR,		// A resource descriptor (used by the Plug-n-Play hardware
										// enumeration and configuration)

	REG_RESOURCE_REQUIREMENTS_LIST,		// A resource requirements list (used by the Plug-n-Play hardware
										// enumeration and configuration)

	REG_QWORD,							// A QWORD value, a 64-bit integer (either big- or little-endian,
										// or unspecified) (introduced in Windows XP)

	REG_QWORD_LITTLE_ENDIAN,			//
	;
}
