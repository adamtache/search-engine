This is a search engine project for Google CodeU (a mentorship program I participated in for the last six months). The search engine supports multiple term query searching and boolean queries using the Vector Space and Standard Boolean models of information retrieval. 

The VSM is implemented in order to create real-valued document vectors of TF-IDF (termfrequency inverse document frequency) weights, with the cosine of the angle between the query and document vectors used as relevance for ranking.

The search engine is configured up to a Redis database which pre-stores crawled Wikipedia and YouTube pages along with TF-IDF calculations to provide quick lookups for query results.
