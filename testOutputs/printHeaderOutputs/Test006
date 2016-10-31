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

	__A_VT* __vptr;

	__A();

	static Class __class();

	static int32_t hashCode(A);

	static __A_VT __vtable;

	static void setFld(A,String);
	static void almostSetFld(String);
	static String getFld(A);

	};

	struct __A_VT
	{

	Class __isa;
	void (*setFld)(A,String);
	void (*almostSetFld)(String);
	String (*getFld)(A);
	int32_t (*hashCode)(A);
	Class (*getClass)(A);
	bool (*equals)(A,Object);

	__A_VT()
	: __isa(__A::__class()),
		setFld(&__A::setFld),
		almostSetFld(&__A::almostSetFld),
		getFld(&__A::getFld),
		hashCode(&__A::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};



	}
}

