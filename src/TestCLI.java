class TestCLI{
	public static void main(String args[]){
		CLIOptions cli= new CLIOptions(args);
		try{
			cli.parseArgs();
		}
		catch(IllegalArgumentException e){
			cli.blurtHelp(""+e);
		}
		finally{
			cli.blurtValues();
		}
	}
}