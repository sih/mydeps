similar=MATCH (a:App)-[:COMPILE]->(l:Artifact)<-[:COMPILE]-(b:App) WITH a,b,l,count(l) AS cnt WHERE length(a-[:COMPILE]->()) = cnt RETURN a,b,l