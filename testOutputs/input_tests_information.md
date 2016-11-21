# General Information
- try{..}catch(...) added
    - need to review/check the placements
        
# Unit testing
- Information
    - currently in the testing implementation methodCounts counts constructors as methods. 
        - change so that methodCounts does not count constructors as methods if time allows.
- Testing
    - AstTraversalTest (0-17)
    - CppTest (0-17)
    - HeaderAstTest (0-17)
    - HeaderTest (0-17)

# Test010 and Test012 fail due to wrong ClassCastException logic
- I have commented out throw ClassCastException and have left the structure. Test010 and Test012 pass. Test016 fails.
- B extends A
    - Can cast B to A, but can't cast A to B
        - How do we check this in the output.cpp file?
        - Same logic applies to Test016

# These are all strange and static (18-20)
- Test018:
	- printMain: int should be int32_t
    - should x be moved to somewhere else since it is global
- Test019:
	- printMain: int should be int32_t
- Test020:
	- how is this supposed to be
	
# 21 - 50?
	
