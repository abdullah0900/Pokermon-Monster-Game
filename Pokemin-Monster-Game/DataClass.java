import java.util.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;

public class DataClass {
    
    private ArrayList<BaseAbility> abilityList = new ArrayList<BaseAbility>();
    private ArrayList<NonVolatileStatus> nonvolatilestatusList = new ArrayList<NonVolatileStatus>();
    private ArrayList<BaseMove> moveList = new ArrayList<BaseMove>();
    private ArrayList<String> dexList = new ArrayList<String>();
    private ArrayList<BasePokemon> pokedexList = new ArrayList<BasePokemon>();
    private PokemonCollection collection, datacollection;
    private ArrayList<ActivePokemon> team1List = new ArrayList<ActivePokemon>();
    private ArrayList<ActivePokemon> team2List = new ArrayList<ActivePokemon>();
    
    public DataClass() {
        
    }
    
    public PokemonCollection getCollection() {
        return this.collection;
    }
    
    public PokemonCollection getDataCollection() {
        return this.datacollection;
    }
    
    public ArrayList<NonVolatileStatus> getNonVolatileStatusList() {
        return this.nonvolatilestatusList;
    }
    
    public ArrayList<String> getDexList() {
        return this.dexList;
    }
    
    public ArrayList<BasePokemon> getPokedexList() {
        return this.pokedexList;
    }
    
    public ArrayList<ActivePokemon>getTeam1List() {
        return this.team1List;
    }
    
    public ArrayList<ActivePokemon>getTeam2List() {
        return this.team2List;
    }
    
    public void initBasePokemon() 
        throws IOException {
    	// Construct the absolute path to the file
        String currentDirectory = System.getProperty("user.dir");
        String filePath = Paths.get(currentDirectory, "DATA", "Pokestats.txt").toString();
        
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        
        ArrayList<BasePokemon> pokeList = new ArrayList<BasePokemon>();
        String name,type1;
        String type2 = "";
        int dexnum,instancenum;
        int basehp,baseatk,basedef,basespatk,basespdef,basespeed;
        boolean secondType = false;
        int numPerPage = 32;
        for (int i=0;i<973;i++) {
            name = sc.next();
            while (!sc.hasNextInt())
                name += " " + sc.next();
            dexnum = Integer.parseInt(sc.next());
            basehp = Integer.parseInt(sc.next());
            baseatk = Integer.parseInt(sc.next());
            basedef = Integer.parseInt(sc.next());
            basespatk = Integer.parseInt(sc.next());
            basespdef = Integer.parseInt(sc.next());
            type1 = sc.next();
            if (!sc.hasNextInt()) {
                type2 = sc.next();
                secondType = true;
            }
            basespeed = Integer.parseInt(sc.next());
            instancenum = Integer.parseInt(sc.next());
            if (secondType) {
                BasePokemon newPoke = new BasePokemon(name,dexnum,instancenum,basehp,baseatk,basedef,basespatk,basespdef,basespeed,type1,type2);
                pokeList.add(newPoke);
            }
            else {
                BasePokemon newPoke = new BasePokemon(name,dexnum,instancenum,basehp,baseatk,basedef,basespatk,basespdef,basespeed,type1);
                pokeList.add(newPoke);
            }
            secondType = false;
        } 
        this.collection = new PokemonCollection();
        
        for (BasePokemon pokemon : pokeList) {
            this.collection.addPokemon(pokemon);
        }
        
        ArrayList<Integer>dexNumList = new ArrayList<Integer>();
        for (BasePokemon pokemon : pokeList) {
            if (!(dexNumList.contains(pokemon.getDexNum()))) {
                dexNumList.add(pokemon.getDexNum());
                this.pokedexList.add(pokemon);
            }
            if (pokemon.getDexNum() >= 721)
                break;
        }
        
        
        this.initBasePokemonData();
        
    }
    
    public void initBasePokemonData() 
        throws IOException
    {
    	// Construct the absolute path to the file "PokemonData.txt"
        String currentDirectory = System.getProperty("user.dir");
        String pokemonDataFilePath = Paths.get(currentDirectory, "DATA", "PokemonData.txt").toString();

        File pokemonDataFile = new File(pokemonDataFilePath);
        Scanner sc = new Scanner(pokemonDataFile); 
        this.datacollection = new PokemonCollection();
        
        String dataname;
        int dataid,dexnum,throwaway3,throwaway4,throwaway5;
        double height, weight;
        boolean isalt,ismega,canchange;
        
        ArrayList<BasePokemon> pokeList = new ArrayList<BasePokemon>();
        ArrayList<String> nameList = new ArrayList<String>();
        pokeList = this.collection.getCollection();
        
        for (BasePokemon pokemon : pokeList) {
            nameList.add(pokemon.getName().toLowerCase());
        }
        
        for (int i=0;i<964;i++) {
            dataid = Integer.parseInt(sc.next());
            dataname = "";
            while (!sc.hasNextInt())
                dataname += sc.next();
            dexnum = Integer.parseInt(sc.next());
            height = (Double.parseDouble(sc.next()))/10;
            weight = (Double.parseDouble(sc.next()))/10;
            throwaway3 = Integer.parseInt(sc.next());
            throwaway4 = Integer.parseInt(sc.next());
            throwaway5 = Integer.parseInt(sc.next());
            
            if (dataid > 10000) {isalt = true;}
            else {isalt = false;}
            
            if (dataname.contains("mega") && isalt) {ismega = true;}
            else {ismega = false;}
            
            if (isalt && ((dataid == 10026) || (dataid == 10117) || ismega)) {canchange = true;}
            else {canchange = false;}
            
            if (nameList.contains(dataname)) {
                BasePokemon pokemon = this.collection.getPokemon(nameList.indexOf(dataname));
                if (pokemon.getDexNum() == dexnum) {
                    pokemon.addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(pokemon);
                }
            }
            else {
                
                //special cases
                
                if (dataname.equals("nidoran-f")) {
                    this.collection.getPokemon(nameList.indexOf("nidoran-female")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("nidoran-female")));
                }
                if (dataname.equals("nidoran-m")) {
                    this.collection.getPokemon(nameList.indexOf("nidoran-male")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("nidoran-male")));
                }
                if (dataname.equals("farfetchd")) {
                    this.collection.getPokemon(nameList.indexOf("farfetch'd")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("farfetch'd")));
                }
                if (dataname.equals("mr-mime")) {
                    this.collection.getPokemon(nameList.indexOf("mr._mime")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("mr._mime")));
                }
                if (dataname.equals("deoxys-normal")) {
                    this.collection.getPokemon(nameList.indexOf("deoxys")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("deoxys")));
                }
                if (dataname.equals("burmy")) {
                    this.collection.getPokemon(nameList.indexOf("burmy-plant")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("burmy-plant")));
                }
                if (dataname.equals("cherrim")) {
                    this.collection.getPokemon(nameList.indexOf("cherrim-overcast")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("cherrim-overcast")));
                }
                if (dataname.equals("shellos")) {
                    this.collection.getPokemon(nameList.indexOf("shellos-east")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("shellos-east")));
                }
                if (dataname.equals("gastrodon")) {
                    this.collection.getPokemon(nameList.indexOf("gastrodon-east")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("gastrodon-east")));
                }
                if (dataname.equals("mime-jr")) {
                    this.collection.getPokemon(nameList.indexOf("mime_jr.")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("mime_jr.")));
                }
                if (dataname.equals("unfezant")) {
                    this.collection.getPokemon(nameList.indexOf("unfezant-male")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("unfezant-male")));
                }
                if (dataname.equals("basculin-red-striped")) {
                    this.collection.getPokemon(nameList.indexOf("basculin-red")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("basculin-red")));
                }
                if (dataname.equals("basculin-blue-striped")) {
                    this.collection.getPokemon(nameList.indexOf("basculin-blue")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("basculin-blue")));
                }
                if (dataname.equals("darmanitan-standard")) {
                    this.collection.getPokemon(nameList.indexOf("darmanitan")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("darmanitan")));
                }
                if (dataname.equals("deerling")) {
                    this.collection.getPokemon(nameList.indexOf("deerling-spring")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("deerling-spring")));
                }
                if (dataname.equals("sawsbuck")) {
                    this.collection.getPokemon(nameList.indexOf("sawsbuck-spring")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("sawsbuck-spring")));
                }
                if (dataname.equals("jellicent")) {
                    this.collection.getPokemon(nameList.indexOf("jellicent-male")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("jellicent-male")));
                }
                if (dataname.equals("frillish")) {
                    this.collection.getPokemon(nameList.indexOf("frillish-male")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("frillish-male")));
                }
                if (dataname.equals("tornadus-incarnate")) {
                    this.collection.getPokemon(nameList.indexOf("tornadus")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("tornadus")));
                }
                if (dataname.equals("thundurus-incarnate")) {
                    this.collection.getPokemon(nameList.indexOf("thundurus")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("thundurus")));
                }
                if (dataname.equals("landorus-incarnate")) {
                    this.collection.getPokemon(nameList.indexOf("landorus")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("landorus")));
                }
                if (dataname.equals("keldeo-ordinary")) {
                    this.collection.getPokemon(nameList.indexOf("keldeo")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("keldeo")));
                }
                if (dataname.equals("pyroar")) {
                    this.collection.getPokemon(nameList.indexOf("pyroar-male")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("pyroar-male")));
                }
                if (dataname.equals("type-null")) {
                    this.collection.getPokemon(nameList.indexOf("type_null")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("type_null")));
                }
                if (dataname.equals("tapu-koko")) {
                    this.collection.getPokemon(nameList.indexOf("tapu_koko")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("tapu_koko")));
                }
                if (dataname.equals("tapu-lele")) {
                    this.collection.getPokemon(nameList.indexOf("tapu_lele")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("tapu_lele")));
                }
                if (dataname.equals("tapu-bulu")) {
                    this.collection.getPokemon(nameList.indexOf("tapu_bulu")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("tapu_bulu")));
                }
                if (dataname.equals("tapu-fini")) {
                    this.collection.getPokemon(nameList.indexOf("tapu_fini")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("tapu_fini")));
                }
                if (dataname.equals("charizard-mega-x")) {
                    this.collection.getPokemon(nameList.indexOf("charizard-mega_x")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("charizard-mega_x")));
                }
                if (dataname.equals("charizard-mega-y")) {
                    this.collection.getPokemon(nameList.indexOf("charizard-mega_y")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("charizard-mega_y")));
                }
                if (dataname.equals("mewtwo-mega-x")) {
                    this.collection.getPokemon(nameList.indexOf("mewtwo-mega_x")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("mewtwo-mega_y")));
                }
                if (dataname.equals("mewtwo-mega-y")) {
                    this.collection.getPokemon(nameList.indexOf("mewtwo-mega_y")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("mewtwo-mega_y")));
                }
                if (dataname.equals("zygarde-10")) {
                    this.collection.getPokemon(nameList.indexOf("zygarde-10%")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("zygarde-10%")));
                }
                if (dataname.equals("zygarde-50")) {
                    this.collection.getPokemon(nameList.indexOf("zygarde")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("zygarde")));
                }
                if (dataname.equals("oricorio")) {
                    this.collection.getPokemon(nameList.indexOf("oricorio-baile")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("oricorio-baile")));
                }
                if (dataname.equals("oricorio-pau")) {
                    this.collection.getPokemon(nameList.indexOf("oricorio-pa'u")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("oricorio-pa'u")));
                }
                if (dataname.equals("necrozma-dusk")) {
                    this.collection.getPokemon(nameList.indexOf("necrozma-dusk_mane")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("necrozma-dusk_mane")));
                }
                if (dataname.equals("necrozma-dawn")) {
                    this.collection.getPokemon(nameList.indexOf("necrozma-dawn_wings")).addData(dataname,dataid,height,weight,isalt,ismega,canchange);
                    this.datacollection.addPokemon(this.collection.getPokemon(nameList.indexOf("necrozma-dawn_wings")));
                }
            }    
        }
    }
    
    public void initBaseMove() 
        throws IOException
    {
    	// Construct the absolute path to the file "Moves.txt"
        String currentDirectory = System.getProperty("user.dir");
        String movesFilePath = Paths.get(currentDirectory, "DATA", "Moves.txt").toString();

        File file = new File(movesFilePath);
        Scanner sc = new Scanner(file);
        
        ArrayList<BaseMove> moveList = new ArrayList<BaseMove>();
        for (int i=0;i<728;i++) {
            int moveid = Integer.parseInt(sc.next());
            String movename = sc.next();
            sc.next();
            int movetype = Integer.parseInt(sc.next());
            int power = Integer.parseInt(sc.next());
            int pp = Integer.parseInt(sc.next());
            double accuracy = Double.parseDouble(sc.next())/100;
            int priority = Integer.parseInt(sc.next());
            int targetid = Integer.parseInt(sc.next());
            int damageid = Integer.parseInt(sc.next());
            int effectid = Integer.parseInt(sc.next());
            double effectchance = Double.parseDouble(sc.next())/100;
            sc.next();
            sc.next();
            sc.next();
            
            BaseMove newmove = new BaseMove(moveid,movename,movetype,power,pp,accuracy,priority,targetid,damageid,effectid,effectchance);
            moveList.add(newmove);
        }
        this.moveList = moveList;
    }
    
    public void initBaseAbility() 
        throws IOException
    {
    	// Construct the absolute path to the file "Abilities.txt"
        String currentDirectory = System.getProperty("user.dir");
        String abilitiesFilePath = Paths.get(currentDirectory, "DATA", "Abilities.txt").toString();

        File file = new File(abilitiesFilePath);
        Scanner sc = new Scanner(file);
        Scanner sc2 = new Scanner(file);
        
        ArrayList<BaseAbility> abilityList = new ArrayList<BaseAbility>();
        for (int i=0;i<233;i++) {
            int abilityid = Integer.parseInt(sc.next());
            String abilityname = sc.next();
            BaseAbility newability = new BaseAbility(abilityid,abilityname);
            abilityList.add(newability);
        }
        this.abilityList = abilityList;
        
    }
    
    public void initNonVolatileStatus() {
        this.nonvolatilestatusList.add(new NonVolatileStatus("Burn", .125, 0.0));
        this.nonvolatilestatusList.add(new NonVolatileStatus("Freeze", 0.0, 0.0));
        this.nonvolatilestatusList.add(new NonVolatileStatus("Paralysis", 0.0, 0.0));
        this.nonvolatilestatusList.add(new NonVolatileStatus("Poison", .125, 0.0));
        this.nonvolatilestatusList.add(new NonVolatileStatus("Badly Poisoned", .0625, .0625));
        this.nonvolatilestatusList.add(new NonVolatileStatus("Sleep", 0.0, 0.0));
    }
    
    public void initDexDescriptions()
        throws IOException
    {
        File file = new File("../DATA/DexDescriptions.txt");
        ArrayList<String> dexList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (int i=0;i<721;i++) {
            String str = "";
            String line = "";
            boolean first = true;
            
            while (true) {
                line = br.readLine();
                if (line.contains("\"") && !first) {
                    str += line;
                    break;
                }
                str += line + "<br>";
                first = false;
            }
            str = str.replace("\"","");
            dexList.add(str);
        }
        this.dexList = dexList;
    }
    
    
    
    public ArrayList<ActivePokemon> teambuilder(int team) 
        throws IOException
    {
    	// Construct the absolute path to the team file based on the team number
        String currentDirectory = System.getProperty("user.dir");
        String teamFilePath = "";
        if (team == 1) {
            teamFilePath = Paths.get(currentDirectory, "Teambuilder", "TeamOne.txt").toString();
        } else if (team == 2) {
            teamFilePath = Paths.get(currentDirectory, "Teambuilder", "TeamTwo.txt").toString();
        } else {
            throw new IllegalArgumentException("Invalid team number: " + team);
        }

        File file = new File(teamFilePath);
        Scanner sc = new Scanner(file);
        Scanner sc2 = new Scanner(file);
        boolean firstrun = true;
        boolean reading = true;
        
        while (reading) {
            String nickname = "";
            String name = "";
            String ability = "";
            
            if (!firstrun) {
                sc.nextLine();
                sc2.nextLine();
                sc2.nextLine();
            }
            
            firstrun = false;
                
            //reading name/nickname
            name = sc.nextLine();
            
            sc2.nextLine();
            
            name = name.substring(0,name.indexOf("@")-1);
            
            while (!sc2.next().equals("Ability:")) {
                sc.next();
            }
            sc.next();
            while (!sc2.next().contains("EVs:")) {
                ability += sc.next() + " ";
            }
            ability = ability.replace(" ","-");
            ability = ability.toLowerCase();
            for (int i=ability.length()-1;i>0;i--) {
                if (ability.charAt(i) == '-')
                    ability = ability.substring(0,i);
                else
                    break;
            }
            
            
            //reading evs
            
            ArrayList<Integer> evs = new ArrayList<Integer>();
            ArrayList<String> evstat = new ArrayList<String>();
            sc.next();
            while (sc2.hasNextInt()) {
                evs.add(Integer.parseInt(sc.next()));
                evstat.add(sc.next());
                sc2.next();
                sc2.next();
                if (sc2.next().equals("/"))
                    sc.next();
            }
            
            //reading nature
            
            String nature = "";
            if (sc2.next().contains("Nature")) {
                nature += sc.next();
            }
            
            sc.next();
            sc2.nextLine();
            String str = sc2.next();
            
            //reading IVs (if not 31)
            ArrayList<Integer> ivs = new ArrayList<Integer>();
            ArrayList<String> ivstat = new ArrayList<String>();
            
            if (str.contains("IVs:")) {
                sc.next();
                
                while (sc2.hasNextInt()) {
                    ivs.add(Integer.parseInt(sc.next()));
                    ivstat.add(sc.next());
                    sc2.next();
                    sc2.next();
                    if (sc2.next().equals("/"))
                        sc.next();
                }
            }
            
            //reading moves (must be 4)
            
            ArrayList<String> moves = new ArrayList<String>();
            String move = "";
            
            sc.nextLine();
            
            move = sc.nextLine();
            
            sc2.nextLine();
            move = move.substring(2,move.length());
            move = move.replace(" ","-");
            move = move.toLowerCase();
            for (int i=move.length()-1;i>0;i--) {
                if (move.charAt(i) == '-')
                    move = move.substring(0,i);
                else
                    break;
            }
            moves.add(move);
            move = sc.nextLine();
            
            sc2.nextLine();
            move = move.substring(2,move.length());
            move = move.replace(" ","-");
            move = move.toLowerCase();
            for (int i=move.length()-1;i>0;i--) {
                if (move.charAt(i) == '-')
                    move = move.substring(0,i);
                else
                    break;
            }
            moves.add(move);
            move = sc.nextLine();
            
            sc2.nextLine();
            move = move.substring(2,move.length());
            move = move.replace(" ","-");
            move = move.toLowerCase();
            for (int i=move.length()-1;i>0;i--) {
                if (move.charAt(i) == '-')
                    move = move.substring(0,i);
                else
                    break;
            }
            moves.add(move);
            move = sc.nextLine();
            
            sc2.nextLine();
            move = move.substring(2,move.length());
            move = move.replace(" ","-");
            move = move.toLowerCase();
            for (int i=move.length()-2;i>0;i--) {
                if (move.charAt(i) == '-')
                    move = move.substring(0,i);
                else
                    break;
            }
            moves.add(move);
            String st = sc.next();
            if (st.equals("end")) {
                reading = false;
            }
            else {
                sc.nextLine();
                sc2.nextLine();
            }
            sc2.next();
            
            //finishing while loop
            
            
            
            //creating ActivePokemon equivalent
            
            int hpiv = 31;
            int atkiv = 31;
            int defiv = 31;
            int spatkiv = 31;
            int spdefiv = 31;
            int speediv = 31;
            int hpev = 0;
            int atkev = 0;
            int defev = 0;
            int spatkev = 0;
            int spdefev = 0;
            int speedev = 0;
            
            for (String s : ivstat) {
                if (s.equals("HP"))
                    hpiv = ivs.get(ivstat.indexOf("HP"));
                if (s.equals("Atk"))
                    atkiv = ivs.get(ivstat.indexOf("Atk"));
                if (s.equals("Def"))
                    defiv = ivs.get(ivstat.indexOf("Def"));
                if (s.equals("SpA"))
                    spatkiv = ivs.get(ivstat.indexOf("SpA"));
                if (s.equals("SpD"))
                    spdefiv = ivs.get(ivstat.indexOf("SpD"));
                if (s.equals("Spe"))
                    speediv = ivs.get(ivstat.indexOf("Spe"));
            }
            for (String s : evstat) {
                if (s.equals("HP"))
                    hpev = evs.get(evstat.indexOf("HP"));
                if (s.equals("Atk"))
                    atkev = evs.get(evstat.indexOf("Atk"));
                if (s.equals("Def"))
                    defev = evs.get(evstat.indexOf("Def"));
                if (s.equals("SpA"))
                    spatkev = evs.get(evstat.indexOf("SpA"));
                if (s.equals("SpD"))
                    spdefev = evs.get(evstat.indexOf("SpD"));
                if (s.equals("Spe"))
                    speedev = evs.get(evstat.indexOf("Spe"));
            }
            
            BasePokemon basepokemon = this.collection.searchOneByName(name);
            ArrayList<BaseMove> realMoves = new ArrayList<BaseMove>();
            
            boolean check = false;
            
            //initializing ActivePokemon into an arraylist to be shared with Main
            
            
            
            for (String fakemove : moves) {
                for (BaseMove actualmove : basepokemon.getPossibleMoves()) {
                    if (fakemove.equals(actualmove.getDataName())) {
                        realMoves.add(actualmove);
                        check = true;
                    }      
                }
                if (!check)
                    System.out.println(basepokemon.getName() + " [" + basepokemon.getDataID() + "] cannot learn " + fakemove + ".");
                check = false;
            }
            
            String temp = "";
            
            for (BaseAbility baseability : basepokemon.getPossibleAbilities()) {
                if (ability.equals(baseability.getDataName()))
                    temp = ability;
            }
            
            if (temp.equals(""))
                System.out.println(basepokemon.getName() + " cannot have " + ability + " as an ability.");
            
            
            
            try {
                ActivePokemon newActive = new ActivePokemon(nickname,nature, temp, 100, realMoves, basepokemon, hpiv, atkiv, 
                                                            defiv, spatkiv, spdefiv, speediv, hpev, atkev, defev, 
                                                            spatkev, spdefev, speedev);
                if (team == 1)
                    this.team1List.add(newActive);
                else if (team == 2)
                    this.team2List.add(newActive);
                
            } catch (Exception e) {
                System.out.println("Error trying to load Pokemon " + name);
            }
            
            
        }
        if (team==1)
            return this.team1List;
        else if (team==2)
            return this.team2List;
        return null;
    }
    
    
    public void merge() 
        throws IOException
    {
        //creating a list of Possible Abilities within each individual BasePokemon object
        
    	// Construct the absolute path to the file "PossibleAbilities.txt"
        String currentDirectory = System.getProperty("user.dir");
        String possibleAbilitiesFilePath = Paths.get(currentDirectory, "DATA", "PossibleAbilities.txt").toString();

        File abilitiesFile = new File(possibleAbilitiesFilePath);
        Scanner abilitiesScanner = new Scanner(abilitiesFile);

        ArrayList<BasePokemon> dataList = new ArrayList<BasePokemon>();
        ArrayList<Integer> dataidList = new ArrayList<Integer>();
        dataList = this.datacollection.getCollection();
        for (BasePokemon pokemon : dataList)
            dataidList.add(pokemon.getDataID());
        
        // Read data from PossibleAbilities.txt
        for (int i = 0; i < 2167; i++) {
            int dataid = Integer.parseInt(abilitiesScanner.next());
            int abilityid = Integer.parseInt(abilitiesScanner.next());
            abilitiesScanner.next();
            abilitiesScanner.next();
            if (dataidList.contains(dataid)) {
                dataList.get(dataidList.indexOf(dataid)).addAbilities(this.abilityList.get(abilityid - 1));
            }
        }

        // Construct the absolute path to the file "PossibleMoves.txt"
        String possibleMovesFilePath = Paths.get(currentDirectory, "DATA", "PossibleMoves.txt").toString();
        File movesFile = new File(possibleMovesFilePath);
        Scanner movesScanner = new Scanner(movesFile);

        // Read data from PossibleMoves.txt
        int moveid;
        for (int i = 0; i < 465817; i++) {
            int dataid = Integer.parseInt(movesScanner.next());
            movesScanner.next();
            moveid = Integer.parseInt(movesScanner.next());
            movesScanner.next();
            movesScanner.next();
            if (dataidList.contains(dataid)) {
                dataList.get(dataidList.indexOf(dataid)).addMoves(this.moveList.get(moveid - 1));
            }
        }
    }
    
}
