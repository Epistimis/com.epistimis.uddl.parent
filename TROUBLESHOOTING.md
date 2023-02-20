If you have problems building any of the projects in these repos, try some combination of the following:

# Focus on Errors First
1. Most of these proejcts will generate warnings and infos. Address errors first. Nothing should ever make it into the master branch with compile errors,
so you shouldn't worry about anything but the errors when building. Sometimes errors occur because of the build order. Make sure Project -> Build Automatically is checked and then try Project-> Clean -> Clean All 
Projects. This will rebuild everything. Sometimes this will clean up residual errors.

# Unresolved References
1. These are typically because either there is a missing dependency in the MANIFEST.MF file *or* because the Java Build Path doesn't reference something 
it should. The MANIFEST.MF file is checked into the Git repo, so that shouldn't be the problem. So check the Java Build Path first: 
1. Select the affected project
and right click to get the context menu -> 
1. Select Properties, then ‘Java Build Path’
2. Select the 'Libraries' tab, then ‘Classpath’. The Classpath should list 'Plug-in Dependencies'.  If it does not,  and click ‘Add Library’ then 'Plug-in Dependencies' then 'Finish', 
3. If 'Plug-in Dependencies' is already listed on the Classpath, you can specify the particular missing project you need by
selecting the 'Projects' tab, then 'Classpath' then click 'Add' and select the missing project, then 'OK'.

# referencedResource the path is unmapped
See https://www.eclipse.org/forums/index.php/t/1078446/
The problem here is the classpath. Set the Java Build Path for the project that needs the referencedResource. To fix it: 

Right mouse -> Properties ‘Java Build Path’ - Select ‘Classpath’ and click ‘Add’ the select the project that generates the needed resource.

# Errors that just won't go away
1. Sometimes Eclipse leaves residue in the Problems pane. If you see errors that don't see valid, **they may not be**. Select the errors, then right click
to display the context menu and delete them. If they are actual errors, they will be regenerated when the project is rebuilt.
1. If the file looks syntactically correct, close it and reopen it. This is particularly relevant for grammar changes. XText grammar changes may 
be overlooked by edit window caches even if the runtime is restarted. Closing the file appears to flush the cache - the errors are gone when the 
file is reopened in the editor
3. If you still have problems, delete the .metadata directory in the eclipse workspace containing the project. Just be prepared to recreate some 
4. things afterwards.
