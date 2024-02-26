import java.util.*;

public class PokemonCollection 
{
    private String name;
    private Boolean forward = true;
    private ArrayList<BasePokemon> pokeList;
    
    //pokemon collection
    public PokemonCollection(ArrayList<BasePokemon> pokeList)
    {
        this.name = "";
        this.pokeList = pokeList;
    }
    
    public PokemonCollection()
    {
        this.name = "";
        this.pokeList = new ArrayList<BasePokemon>();
    }
    

    //get pokemon
    public BasePokemon getPokemon(int index)  
    {
        return this.pokeList.get(index);
    }
    
    //get size
    public int getSize()
    {
        return this.pokeList.size();
    }
    
    //print collection
    public void printCollection()
    {
        System.out.println("\nCollection: "+this.name+"\n");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < this.pokeList.size(); i++)
        {
            System.out.println((i+1)+": "+this.getPokemon(i).toString());
        }
        System.out.println(this.pokeList.size()+" in collection.");
        System.out.println("--------------------------------------------");
    }
    

    //print summary
    public void printSummary()
    {
        for (int i = 0; i < this.pokeList.size(); i++)
        {
            System.out.println(this.getPokemon(i).description());
        }
    }
    
    //add pokemon
    public void addPokemon(BasePokemon newMon)
    {
        this.pokeList.add(newMon);
    }

    
    public ArrayList<BasePokemon> getCollection()
    {
        return this.pokeList;
    }
    
    //search by name
    public ArrayList<BasePokemon> searchByName (String name)
    {
        
        ArrayList<BasePokemon> searchList = new ArrayList<BasePokemon>();
        for (BasePokemon pokemon : this.pokeList) {
            if (pokemon.getName().contains(name))
                searchList.add(pokemon);
        }
        return searchList;
    }
    
    public BasePokemon searchOneByName(String name) {
        for (BasePokemon pokemon : this.pokeList) {
            if (name.equals(pokemon.getName()))
                return pokemon;
        }
        return null;
    }
    
    //search by type
    public ArrayList<BasePokemon> searchByType (String type)
    {
        ArrayList<BasePokemon> searchList = new ArrayList<BasePokemon>();
        for (BasePokemon pokemon : pokeList) {
            if (pokemon.getType1().equals(type) || pokemon.getType2().equals(type))
                searchList.add(pokemon);
        }
        return searchList;
    }
    
    public ArrayList<BasePokemon> searchByType(String type, String type2)
    {
        ArrayList<BasePokemon> searchList = new ArrayList<BasePokemon>();
        for (BasePokemon pokemon : this.pokeList) {
            if (pokemon.getType1().equals(type) && pokemon.getType2().equals(type2))
                searchList.add(pokemon);
            if (pokemon.getType1().equals(type2) && pokemon.getType2().equals(type))
                searchList.add(pokemon);
        }
        return searchList;
    }
    
    //sorting
    public ArrayList<BasePokemon> sortByBST(ArrayList<BasePokemon> list) {
        ArrayList<BasePokemon> lesser = new ArrayList<BasePokemon>();
        ArrayList<BasePokemon> greater = new ArrayList<BasePokemon>();
        if (list.size() >= 1) {
            BasePokemon pivot = list.get(list.size()-1);
            for (int i = 0; i < list.size()-1; i++) {
                if (!this.forward) {
                    if (list.get(i).baseStatTotal() > pivot.baseStatTotal())
                        lesser.add(list.get(i));    
                    else
                        greater.add(list.get(i));   
                }
                else
                {
                    if (list.get(i).baseStatTotal() < pivot.baseStatTotal())
                        lesser.add(list.get(i));    
                    else
                        greater.add(list.get(i));   
                }
            }
            lesser = sortByBST(lesser);
            greater = sortByBST(greater);
            lesser.add(pivot);
            lesser.addAll(greater);
            return lesser;
        }
        return list;
    }
    
    //sort by dexsum
    public ArrayList<BasePokemon> sortByDexNum(ArrayList<BasePokemon> list) {
        ArrayList<BasePokemon> lesser = new ArrayList<BasePokemon>();
        ArrayList<BasePokemon> greater = new ArrayList<BasePokemon>();
        if (list.size() >= 1) {
            BasePokemon pivot = list.get(list.size()-1);
            for (int i = 0; i < list.size()-1; i++) {
                if (!this.forward) {
                    if (list.get(i).getInstanceNum() > pivot.getInstanceNum())
                        lesser.add(list.get(i));    
                    else
                        greater.add(list.get(i));   
                }
                else
                {
                    if (list.get(i).getInstanceNum() < pivot.getInstanceNum())
                        lesser.add(list.get(i));    
                    else
                        greater.add(list.get(i));   
                }
            }
            lesser = sortByDexNum(lesser);
            greater = sortByDexNum(greater);
            lesser.add(pivot);
            lesser.addAll(greater);
            return lesser;
        }
        
        return list;
    }
    
    //sort by alpha
    public ArrayList<BasePokemon> sortByAlpha(ArrayList<BasePokemon> list) {
        ArrayList<String> alphaList = new ArrayList<String>();
        ArrayList<BasePokemon> returnList = new ArrayList<BasePokemon>();
        for (BasePokemon pokemon : list)
            alphaList.add(pokemon.getName());
        Collections.sort(alphaList);
        for (int j=0;j<alphaList.size();j++) {
            for (int i=0;i<list.size();i++) {
                if (list.get(i).getName().equals(alphaList.get(j))) {
                    returnList.add(list.get(i));
                    continue;
                }
            }
        }
        if (!this.forward)
            Collections.reverse(returnList);
        return returnList;
    }
    
    public ArrayList<BasePokemon> reverseList(ArrayList<BasePokemon> list) {
        Collections.reverse(list);
        if (this.forward)
            this.forward = false;
        else
            this.forward = true;
        return list;
    }
}