#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test006 {


	struct __A;
	struct __A_VT;

	typedef __A* A;

	struct __A
	{

	String fld;

	__A();

	__A_VT* __vptr;

	static __A_VT __vtable;

	static void setFld(String);
	static void almostSetFld(String);
	static String getFld(A);

	};

	struct __A_VT
	{

	Class __isa;
	void (*setFld)(String);
	void (*almostSetFld)(String);
	String (*getFld)(A);
	int32_t (*hashCode)(Object);
	Class (*getClass)(Object);
	bool (*equals)(A,Object);

	__A_VT()
	: __isa(__A::__class()),
		setFld(&__A::setFld),
		almostSetFld(&__A::almostSetFld),
		getFld(&__A::getFld),
		hashCode((int_32t(*)(A)) &__Object::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};



	}
}

